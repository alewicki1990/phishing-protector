package com.github.lewicki1990.phishingprotector.smsprocessing.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lewicki1990.phishingprotector.message.Sms;

public interface SmsMapper {
    SmsDTO convertSmsToSmsDTO(Sms sms) throws JsonProcessingException;

    SmsAttributes transformJsonIntoSmsAttributes(Sms sms) throws JsonProcessingException;

    Sms convertSmsDTOToSMS(SmsDTO smsDTO) throws JsonProcessingException;

    String transformSmsAttributesIntoJson(SmsDTO smsDTO) throws JsonProcessingException;

}
