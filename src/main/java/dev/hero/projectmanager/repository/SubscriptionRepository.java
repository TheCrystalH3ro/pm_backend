package dev.hero.projectmanager.repository;

import dev.hero.projectmanager.model.Organization;
import dev.hero.projectmanager.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>
{
    Optional<Subscription> findByOrganization(Organization organization);
    Optional<Subscription> findByStripeCustomerId(String stripeCustomerId);
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
