package com.github.lewicki1990.phishingprotector.smsprocessing.strategy;

import com.github.lewicki1990.phishingprotector.antiphishingsubscription.ProtectionSubscriptionService;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public class ActivatedSmsDTOAntiPhishingSubscriptionProcessingStrategy implements SmsDTOProcessingStrategy {

    private final ProtectionSubscriptionService protectionSubscriptionService;
    @Override
    public SmsDTO process(SmsDTO smsDTO) {
        if(!protectionSubscriptionService.isActiveForMsisdn(smsDTO.getRecipient())){
            log.debug("[smsId={}] recipient has active subscription and will be processed in next step.", smsDTO.getId());
            return smsDTO;
        } else {
            return new NullSmsDTO();
        }
    }
}
