package dev.hero.projectmanager.service;

import dev.hero.projectmanager.dto.request.CreateOrganizationRequest;
import dev.hero.projectmanager.dto.request.InviteMemberRequest;
import dev.hero.projectmanager.dto.response.InvitationResponse;
import dev.hero.projectmanager.dto.response.OrganizationResponse;
import dev.hero.projectmanager.dto.response.UserResponse;
import dev.hero.projectmanager.exception.AppException;
import dev.hero.projectmanager.mapper.PmMapper;
import dev.hero.projectmanager.model.*;
import dev.hero.projectmanager.repository.*;
import dev.hero.projectmanager.security.CurrentUserProvider;
import dev.hero.projectmanager.util.SlugUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizationService extends OrgAwareService
{
    private final OrganizationRepository orgRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final InvitationRepository invitationRepository;
    private final PmMapper mapper;
    private final JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public OrganizationService(OrganizationRepository orgRepository, UserRepository userRepository, SubscriptionRepository subscriptionRepository, InvitationRepository invitationRepository, PmMapper mapper, JavaMailSender mailSender, CurrentUserProvider currentUserProvider)
    {
        super(currentUserProvider);

        this.orgRepository = orgRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.invitationRepository = invitationRepository;
        this.mapper = mapper;
        this.mailSender = mailSender;
    }

    @Transactional
    public OrganizationResponse createOrganization(CreateOrganizationRequest request)
    {
        User user = getCurrentUser();

        if (user.getOrganization() != null) {
            throw new AppException("You are already part of an organization", HttpStatus.CONFLICT);
        }

        String slug = SlugUtils.generateUniqueSlug(request.name(), orgRepository);

        Organization org = new Organization();
        org.setName(request.name());
        org.setSlug(slug);
        org.setPlan(Plan.TRIAL);
        org.setTrialEndsAt(LocalDateTime.now().plusDays(14));
        orgRepository.save(org);

        user.setOrgRole(OrgRole.OWNER);
        user.setOrganization(org);
        userRepository.save(user);

        Subscription subscription = new Subscription();
        subscription.setOrganization(org);
        subscription.setStatus(SubscriptionStatus.TRIALING);
        subscriptionRepository.save(subscription);

        return mapper.toOrganizationResponse(org, 1);
    }

    public OrganizationResponse getMyOrganization()
    {
        Organization org = getCurrentOrg();
        int memberCount = userRepository.findByOrganization(org).size();
        return mapper.toOrganizationResponse(org, memberCount);
    }

    public List<UserResponse> getMembers()
    {
        Organization org = getCurrentOrg();
        return userRepository.findByOrganization(org)
                .stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InvitationResponse inviteMember(InviteMemberRequest request)
    {
        requireRole(OrgRole.OWNER, OrgRole.ADMIN);
        Organization org = getCurrentOrg();

        if (!isPremium()) {
            long memberCount = userRepository.findByOrganization(org).size();
            if (memberCount >= 5) {
                throw new AppException("Free plan is limited to 5 members. Upgrade to Premium for unlimited members.", HttpStatus.PAYMENT_REQUIRED);
            }
        }

        userRepository.findByEmail(request.email()).ifPresent(u -> {
            if (org.equals(u.getOrganization())) {
                throw new AppException("User is already a member of this organization", HttpStatus.CONFLICT);
            }
        });

        invitationRepository.findByEmailAndOrganization(request.email(), org)
                .ifPresent(inv -> {
                    if (!inv.isAccepted() && inv.getExpiresAt().isAfter(LocalDateTime.now())) {
                        throw new AppException("An invitation has already been sent to this email", HttpStatus.CONFLICT);
                    }
                });

        Invitation invitation = new Invitation();
        invitation.setEmail(request.email());
        invitation.setRole(request.role() != null ? request.role() : OrgRole.MEMBER);
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7));
        invitation.setOrganization(org);
        invitation.setInvitedBy(getCurrentUser());

        invitationRepository.save(invitation);

        sendInvitationEmail(invitation, org);

        return mapper.toInvitationResponse(invitation);
    }

    @Transactional
    public void acceptInvitation(String token)
    {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new AppException("Invitation not found or already used", HttpStatus.NOT_FOUND));

        if (invitation.isAccepted())
            throw new AppException("Invitation already accepted", HttpStatus.CONFLICT);

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new AppException("Invitation has expired", HttpStatus.GONE);

        User user = getCurrentUser();
        if (user.getOrganization() != null)
            throw new AppException("You are already part of an organization", HttpStatus.CONFLICT);

        user.setOrganization(invitation.getOrganization());
        user.setOrgRole(invitation.getRole());
        userRepository.save(user);

        invitation.setAccepted(true);
        invitationRepository.save(invitation);
    }

    @Transactional
    public void removeMember(Long userId)
    {
        requireRole(OrgRole.OWNER, OrgRole.ADMIN);
        Organization org = getCurrentOrg();

        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (!org.equals(userToRemove.getOrganization()))
            throw new AppException("User is not in your organization", HttpStatus.FORBIDDEN);

        if (userToRemove.getOrgRole() == OrgRole.OWNER)
            throw new AppException("Cannot remove the organization owner", HttpStatus.FORBIDDEN);

        userToRemove.setOrganization(null);
        userToRemove.setOrgRole(OrgRole.MEMBER);

        userRepository.save(userToRemove);
    }

    public List<InvitationResponse> getPendingInvitations()
    {
        requireRole(OrgRole.OWNER, OrgRole.ADMIN);

        Organization org = getCurrentOrg();

        return invitationRepository.findByOrganizationAndAccepted(org ,false)
                .stream()
                .filter(inv -> inv.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(mapper::toInvitationResponse)
                .collect(Collectors.toList());
    }

    private void sendInvitationEmail(Invitation invitation, Organization org)
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(invitation.getEmail());
            message.setSubject("You've been invited to join " + org.getName());
            message.setText(
                    "You've been invited to join " + org.getName() + " on ProjectManager.\n\n" +
                    "Click link below to accept the invitation:\n" +
                    frontendUrl + "/invite/" + invitation.getToken() + "\n\n" +
                    "This invitation expires in 7 days.\n\n" +
                    "If you don't have an account yet, you'll be prompted to create one."
            );

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send invitation email: " + e.getMessage());
        }
    }
}
