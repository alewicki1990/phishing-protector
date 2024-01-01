package com.github.lewicki1990.phishingprotector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class SmsMapper {

    private final ObjectMapper objectMapper;

    public SmsMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public SmsDTO convertSmsToSmsDTO(Sms sms) throws JsonProcessingException {
        SmsAttributes smsAttributes = transformJsonIntoSmsAttributes(sms);

        long sender = Long.parseLong(smsAttributes.getSender());
        long recipient = Long.parseLong(smsAttributes.getRecipient());

        return new SmsDTO.SmsDTOBuilder().id(sms.getId())
                                         .sender(sender)
                                         .recipient(recipient)
                                         .message(smsAttributes.getMessage())
                                         .build();
    }

    private SmsAttributes transformJsonIntoSmsAttributes(Sms sms) throws JsonProcessingException {
        return objectMapper.readValue(sms.getAttributesJson(), SmsAttributes.class);
    }


    public Sms convertSmsDTOToSMS(SmsDTO smsDTO) throws JsonProcessingException {
        String smsAttributesJson = transformSmsAttributesIntoJson(smsDTO);
        return new Sms(smsDTO.getId(), smsAttributesJson);
    }

    private String transformSmsAttributesIntoJson(SmsDTO smsDTO) throws JsonProcessingException {
        String sender = String.valueOf(smsDTO.getSender());
        String recipient = String.valueOf(smsDTO.getRecipient());

        SmsAttributes smsAttributes = new SmsAttributes(sender, recipient, smsDTO.getMessage());

        return objectMapper.writeValueAsString(smsAttributes);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SmsAttributes {
        private String sender;

        private String recipient;

        private String message;
    }
}
