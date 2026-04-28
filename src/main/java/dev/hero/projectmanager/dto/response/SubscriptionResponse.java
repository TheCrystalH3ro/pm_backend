package dev.hero.projectmanager.dto.response;

import dev.hero.projectmanager.model.Plan;
import dev.hero.projectmanager.model.SubscriptionStatus;

import java.time.LocalDateTime;

public record SubscriptionResponse(
        Plan plan,
        SubscriptionStatus status,
        LocalDateTime currentPeriodEnd,
        boolean isTrialing,
        long daysLeftInTrial
) {
}
