package com.github.lewicki1990.phishingprotector.smsprocessing;

import com.github.lewicki1990.phishingprotector.smsprocessing.mapping.SmsDTO;
import com.github.lewicki1990.phishingprotector.antiphishingsubscription.ProtectionSubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@AllArgsConstructor
@Service
public class SmsProcessingService {

    private ProtectionSubscriptionService protectionSubscriptionService;

    // TODO: Create strategy class, find how to make this code more readable (!!!)
    void processSmsDTOInContextOfManagingAntiPhishingSubscription(SmsDTO smsDTO){
        long smsId = smsDTO.getId();
        long sender = smsDTO.getSender();
        long recipient = smsDTO.getRecipient();
        String message = smsDTO.getMessage();

        if(protectionSubscriptionService.isMsisdnEqualToSubscriptionManagementPhoneNumber(recipient)){
            if(protectionSubscriptionService.isMessageEqualSubscriptionActivationMessage(message)){
                protectionSubscriptionService.setSubscriptionActive(sender,smsId);
            } else if (protectionSubscriptionService.isMessageEqualSubscriptionDeactivationMessage(message)){
                protectionSubscriptionService.setSubscriptionInactive(sender,smsId);
            } else {
                log.warn("[smsId={}, msisdn={}] Unknown command sent: {}", smsId, sender, recipient);
            }
        } else {
            log.info("[smsId={}, msisdn={}] Normal message not to activate service, recipient={}, message={}", smsId, sender, recipient, message);
        }
    }
}