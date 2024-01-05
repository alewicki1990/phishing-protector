package com.github.lewicki1990.phishingprotector.antiphishingsubscription;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Log4j2
@Service
public class ProtectionSubscriptionService {

    private final long subscriptionManagementPhoneNumber;

    private final String correctSubscriptionActivationMessage;

    private final String correctSubscriptionDeactivationMessage;

    private final ProtectionSubscriptionRepository protectionSubscriptionRepository;

    @Autowired
    public ProtectionSubscriptionService(@Value("${anti-phishing-subscription.phone-number}") long subscriptionManagementPhoneNumber,
                                         @Value("${anti-phishing-subscription.activation-message}") String correctSubscriptionActivationMessage,
                                         @Value("${anti-phishing-subscription.deactivation-message}") String correctSubscriptionDeactivationMessage,
                                         ProtectionSubscriptionRepository protectionSubscriptionRepository) {
        this.subscriptionManagementPhoneNumber = subscriptionManagementPhoneNumber;
        this.correctSubscriptionActivationMessage = correctSubscriptionActivationMessage;
        this.correctSubscriptionDeactivationMessage = correctSubscriptionDeactivationMessage;
        this.protectionSubscriptionRepository = protectionSubscriptionRepository;
    }

    // TODO: Think if it is possible to encapsulate these methods

    public boolean isMsisdnEqualToSubscriptionManagementPhoneNumber(long msisdn) {
        return msisdn == subscriptionManagementPhoneNumber;
    }

    public boolean isMessageEqualSubscriptionActivationMessage(String message) {
        return message.equalsIgnoreCase(correctSubscriptionActivationMessage);
    }

    public boolean isMessageEqualSubscriptionDeactivationMessage(String message) {
        return message.equalsIgnoreCase(correctSubscriptionDeactivationMessage);
    }

    @Transactional(readOnly = true)
    public boolean isActiveForMsisdn(long msisdn){
        Optional<ProtectionSubscription> optionalSubscription = protectionSubscriptionRepository.findByMsisdn(msisdn);

        return optionalSubscription.isPresent() && optionalSubscription.get().isActive();
    }

    // TODO: Create strategy class
    @Transactional
    public void setSubscriptionActive(long msisdnThatSubmittedRequest, long smsId) {
        Optional<ProtectionSubscription> optionalSubscription = protectionSubscriptionRepository.findByMsisdn(msisdnThatSubmittedRequest);

        if (optionalSubscription.isPresent()) {

            ProtectionSubscription currentSubscription = optionalSubscription.get();

            if (currentSubscription.isActive()) {
                log.warn("[smsId={}, msisdn={}] Anti Phishing subscription: Activation has been requested but subscription is already active.", smsId, msisdnThatSubmittedRequest);
            } else {
                currentSubscription.setActive();
                currentSubscription.setChangeDate(LocalDateTime.now());
                protectionSubscriptionRepository.save(currentSubscription);
                log.info("[smsId={}, msisdn={}] Anti Phishing subscription: Changed to active.", smsId, msisdnThatSubmittedRequest);
            }
        } else {
            ProtectionSubscription newSubscription = ProtectionSubscription.builder()
                                                                           .msisdn(msisdnThatSubmittedRequest)
                                                                           .isActive(true)
                                                                           .changeDate(LocalDateTime.now())
                                                                           .build();
            protectionSubscriptionRepository.save(newSubscription);

            log.info("[smsId={}, msisdn={}] Anti Phishing subscription: Created new active subscription.", smsId,msisdnThatSubmittedRequest);
        }
    }

    // TODO: Create strategy class
    @Transactional
    public void setSubscriptionInactive(long msisdnThatSubmittedRequest, long smsId) {
        Optional<ProtectionSubscription> optionalSubscription = protectionSubscriptionRepository.findByMsisdn(msisdnThatSubmittedRequest);

        if (optionalSubscription.isPresent()) {

            ProtectionSubscription subscription = optionalSubscription.get();

            if (subscription.isActive()) {
                subscription.setInactive();
                subscription.setChangeDate(LocalDateTime.now());
                log.info("[smsId={}, msisdn={}] Anti Phishing subscription: Changed to inactive.", smsId, msisdnThatSubmittedRequest);

            } else {
                log.warn("[smsId={}, msisdn={}] Anti Phishing subscription: Deactivation has been requested but subscription is already inactive", smsId, msisdnThatSubmittedRequest);
            }
        } else {
            log.warn("[smsId={}, msisdn={}] Anti Phishing subscription: Subscription never activated before. Request for deactivate denied.", smsId, msisdnThatSubmittedRequest);
        }
    }
}