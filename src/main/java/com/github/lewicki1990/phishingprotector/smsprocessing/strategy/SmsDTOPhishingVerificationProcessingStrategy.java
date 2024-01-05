package com.github.lewicki1990.phishingprotector.smsprocessing.strategy;

import com.github.lewicki1990.phishingprotector.antiphishingsubscription.MessageContentAnalyzer;
import com.github.lewicki1990.phishingprotector.phishingchecker.PhishingCheckerService;
import com.github.lewicki1990.phishingprotector.smssource.SmsSourceService;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsDTO;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class SmsDTOPhishingVerificationProcessingStrategy implements SmsDTOFinishStrategy {

    private final SmsSourceService smsSourceService;

    private final PhishingCheckerService phishingCheckerService;

    private final MessageContentAnalyzer messageContentAnalyzer;

    @Override
    public void process(SmsDTO smsDTO) {
        if(smsDTO == null){
            log.error("Processing smses in context of containing phishing cannot be done because processed object is null");
            throw new IllegalArgumentException();
        }

        String message = smsDTO.getMessage();
        if(message==null){
            log.warn("Cannot extract urls from text because text is null" );
            return;
        }
        Set<String> urls = messageContentAnalyzer.extractUrls(message);

        if(urls.isEmpty()){
            log.info("[smsId={}] No urls found in message.", smsDTO.getId() );
        } else {
            processSmsDTOInContextOfContainingPhishing(smsDTO.getId(), urls);
        }
    }

    private void processSmsDTOInContextOfContainingPhishing(long smsId, Set<String> urls){
        if(urls.stream().anyMatch(phishingCheckerService::isUrlPhishing)){
            log.info("[smsId={}] Sms has url/urls with phishing.", smsId);
                smsSourceService.deleteSmsById(smsId);
               log.info("[smsId={}] Sms deleted from sms table because of phishing.", smsId);
        } else {
            log.info("[smsId={}] No phishing found in links.", smsId);
        }
    }
}