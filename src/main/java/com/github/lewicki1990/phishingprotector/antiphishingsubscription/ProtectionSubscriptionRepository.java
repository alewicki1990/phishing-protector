package com.github.lewicki1990.phishingprotector.antiphishingsubscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProtectionSubscriptionRepository extends JpaRepository<ProtectionSubscription, Long> {
    Optional<ProtectionSubscription> findByMsisdn(long msisdn);
}