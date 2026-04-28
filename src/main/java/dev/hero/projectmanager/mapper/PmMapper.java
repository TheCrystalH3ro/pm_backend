package dev.hero.projectmanager.mapper;

import dev.hero.projectmanager.dto.response.*;
import dev.hero.projectmanager.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class PmMapper
{
    public UserResponse toUserResponse(User user)
    {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getOrgRole()
        );
    }

    public OrganizationResponse toOrganizationResponse(Organization org, int memberCount)
    {
        return new OrganizationResponse(
                org.getId(),
                org.getName(),
                org.getSlug(),
                org.getPlan(),
                org.getTrialEndsAt(),
                memberCount
        );
    }

    public ProjectResponse toProjectResponse(Project project, long taskCount)
    {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getCreatedAt(),
                toUserResponse(project.getCreatedBy()),
                taskCount
        );
    }

    public TaskResponse toTaskResponse(Task task)
    {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getPosition(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getColumn().getId(),
                toUserResponse(task.getCreatedBy()),
                task.getAssignees().stream()
                    .map(this::toUserResponse)
                    .collect(Collectors.toSet())
        );
    }

    public BoardColumnResponse toColumnResponse(BoardColumn column)
    {
        return new BoardColumnResponse(
                column.getId(),
                column.getName(),
                column.getPosition(),
                column.getTasks().stream()
                        .sorted(Comparator.comparingDouble(Task::getPosition))
                        .map(this::toTaskResponse)
                        .collect(Collectors.toList())
        );
    }

    public CommentResponse toCommentResponse(Comment comment)
    {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                toUserResponse(comment.getAuthor())
        );
    }

    public AuditLogResponse toAuditLogResponse(AuditLog log)
    {
        return new AuditLogResponse(
                log.getId(),
                log.getAction(),
                log.getEntityType(),
                log.getEntityId(),
                log.getDetails(),
                log.getCreatedAt(),
                toUserResponse(log.getPerformedBy())
        );
    }

    public InvitationResponse toInvitationResponse(Invitation invitation)
    {
        return new InvitationResponse(
                invitation.getId(),
                invitation.getEmail(),
                invitation.getRole(),
                invitation.isAccepted(),
                invitation.getExpiresAt()
        );
    }

    public SubscriptionResponse toSubscriptionResponse(Organization org, Subscription subscription)
    {
        long daysLeftInTrial = 0;
        boolean isTrialing = org.getPlan() == Plan.TRIAL;

        if (isTrialing && org.getTrialEndsAt() != null)
        {
            daysLeftInTrial = ChronoUnit.DAYS.between(LocalDateTime.now(), org.getTrialEndsAt());
            daysLeftInTrial = Math.max(0, daysLeftInTrial);
        }

        return new SubscriptionResponse(
                org.getPlan(),
                subscription != null ? subscription.getStatus() : SubscriptionStatus.ACTIVE,
                subscription != null ? subscription.getCurrentPeriodEnd() : null,
                isTrialing,
                daysLeftInTrial
        );
    }
}
