package com.github.lewicki1990.phishingprotector.smsprocessing.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lewicki1990.phishingprotector.message.Sms;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SmsMapperImpl implements SmsMapper {

    private final ObjectMapper objectMapper;

    public SmsMapperImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public SmsDTO convertSmsToSmsDTO(Sms sms) throws JsonProcessingException {
        SmsAttributes smsAttributes;
        smsAttributes = transformJsonIntoSmsAttributes(sms);

        long sender = Long.parseLong(smsAttributes.getSender());
        long recipient = Long.parseLong(smsAttributes.getRecipient());

        return new SmsDTO.SmsDTOBuilder().id(sms.getId())
                                         .sender(sender)
                                         .recipient(recipient)
                                         .message(smsAttributes.getMessage())
                                         .build();
    }

    @Override
    public SmsAttributes transformJsonIntoSmsAttributes(Sms sms) throws JsonProcessingException {
        return objectMapper.readValue(sms.getAttributesJson(), SmsAttributes.class);
    }


    @Override
    public Sms convertSmsDTOToSMS(SmsDTO smsDTO) throws JsonProcessingException {
        String smsAttributesJson = transformSmsAttributesIntoJson(smsDTO);
        return new Sms(smsDTO.getId(), smsAttributesJson);
    }

    @Override
    public String transformSmsAttributesIntoJson(SmsDTO smsDTO) throws JsonProcessingException {
        String sender = String.valueOf(smsDTO.getSender());
        String recipient = String.valueOf(smsDTO.getRecipient());

        SmsAttributes smsAttributes = new SmsAttributes(sender, recipient, smsDTO.getMessage());

        return objectMapper.writeValueAsString(smsAttributes);
    }
}