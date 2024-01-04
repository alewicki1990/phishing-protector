package com.github.lewicki1990.phishingprotector.smsprocessing.strategy;

import com.github.lewicki1990.phishingprotector.antiphishingsubscription.ProtectionSubscriptionService;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SmsDTOAntiPhishingSubscriptionProcessingStrategy implements SmsDTOProcessingStrategy{

    private ProtectionSubscriptionService protectionSubscriptionService;

    public SmsDTO process(SmsDTO smsDTO){
        long smsId = smsDTO.getId();
        long sender = smsDTO.getSender();
        long recipient = smsDTO.getRecipient();
        String message = smsDTO.getMessage();

        if(protectionSubscriptionService.isMsisdnEqualToSubscriptionManagementPhoneNumber(recipient)){
            if(protectionSubscriptionService.isMessageEqualSubscriptionActivationMessage(message)){
                protectionSubscriptionService.setSubscriptionActive(sender,smsId);
                return new NullSmsDTO();
            } else if (protectionSubscriptionService.isMessageEqualSubscriptionDeactivationMessage(message)){
                protectionSubscriptionService.setSubscriptionInactive(sender,smsId);
                return new NullSmsDTO();
            } else {
                log.warn("[smsId={}, msisdn={}] Unknown command sent to {}", smsId, sender, recipient);
                return new NullSmsDTO();
            }
        } else {
            log.info("[smsId={}, msisdn={}] Normal message not to activate service, recipient={}, message={}", smsId, sender, recipient, message);
            return smsDTO;
        }
    }
}