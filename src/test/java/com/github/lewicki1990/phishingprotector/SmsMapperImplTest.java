package com.github.lewicki1990.phishingprotector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lewicki1990.phishingprotector.smssource.Sms;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsDTO;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsMapper;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsMapperImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmsMapperImplTest {

    @Test
    void convertingSmsToSmsDTOShouldReturnSmsDTOObjectWithTheSameValuesAsOriginalSms() {
        // given
        long id = 1L;

        long sender = 234100200300L;
        long recipient = 48700800999L;
        String message = "Dzień dobry. W związku z audytem nadzór finansowy w naszym banku proszą o potwierdzanie danych pod adresem: https://www.m-bank.pl.ng/personal-data";
        String attributesJson = getSmsAttributesAsJsonString(sender, recipient, message);

        Sms sms = new Sms(id, attributesJson);

        SmsMapper smsMapper = new SmsMapperImpl();

        // when
        SmsDTO smsDTO;
        try {
            smsDTO =  smsMapper.convertSmsToSmsDTO(sms);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // then
        assertEquals(id, smsDTO.getId());
        assertEquals(sender, smsDTO.getSender());
        assertEquals(recipient, smsDTO.getRecipient());
        assertEquals(message, smsDTO.getMessage());
    }

    private static String getSmsAttributesAsJsonString(long sender, long recipient, String message) {
        return "{\"sender\":\"" + sender + "\"," +
                "\"recipient\":\"" + recipient + "\"," +
                "\"message\":\"" + message + "\"}";
    }


    @Test
    void jsonWithUnknownFieldNameShouldThrowJsonProcessingException() {
        // given
        long id = 1L;
        long sender = 234100200300L;
        long recipient = 48700800999L;
        String message = "Dzień dobry. W związku z audytem nadzór finansowy w naszym banku proszą o potwierdzanie danych pod adresem: https://www.m-bank.pl.ng/personal-data";
        String unknownFieldName = "AAAAAAAAAAAA";

        String jsonWithUnknownFieldName = "{\"sender\":\"" + sender + "\"," +
                "\"" + unknownFieldName + "\":" + recipient + "," +
                "\"message\":\"" + message + "\"}";
        Sms smsWithJsonWithUnknownFieldName = new Sms(id, jsonWithUnknownFieldName);
        SmsMapper smsMapper = new SmsMapperImpl();

        // when + then
        assertThrows(JsonProcessingException.class, () -> smsMapper.convertSmsToSmsDTO(smsWithJsonWithUnknownFieldName));
    }
}