package dev.hero.projectmanager.controller;

import dev.hero.projectmanager.dto.request.CreateOrganizationRequest;
import dev.hero.projectmanager.dto.request.InviteMemberRequest;
import dev.hero.projectmanager.dto.response.InvitationResponse;
import dev.hero.projectmanager.dto.response.OrganizationResponse;
import dev.hero.projectmanager.dto.response.UserResponse;
import dev.hero.projectmanager.service.OrganizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organizations")
public class OrganizationController
{
    private final OrganizationService orgService;

    public OrganizationController(OrganizationService orgService) {
        this.orgService = orgService;
    }

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(@Valid @RequestBody CreateOrganizationRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(orgService.createOrganization(request));
    }

    @GetMapping("/me")
    public ResponseEntity<OrganizationResponse> getMyOrg()
    {
        return ResponseEntity.ok(orgService.getMyOrganization());
    }

    @GetMapping("/members")
    public ResponseEntity<List<UserResponse>> getMembers()
    {
        return ResponseEntity.ok(orgService.getMembers());
    }

    @DeleteMapping("/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long userId)
    {
        orgService.removeMember(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/invite")
    public ResponseEntity<InvitationResponse> invite(@Valid @RequestBody InviteMemberRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(orgService.inviteMember(request));
    }

    @PostMapping("/invite/accept/{token}")
    public ResponseEntity<Void> acceptInvite(@PathVariable String token)
    {
        orgService.acceptInvitation(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<InvitationResponse>> getPendingInvitations()
    {
        return ResponseEntity.ok(orgService.getPendingInvitations());
    }
}
