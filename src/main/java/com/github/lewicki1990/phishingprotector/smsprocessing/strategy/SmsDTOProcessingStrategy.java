package com.github.lewicki1990.phishingprotector.smsprocessing.strategy;

import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsDTO;

public interface SmsDTOProcessingStrategy {
    SmsDTO process(SmsDTO smsDTO);
}
