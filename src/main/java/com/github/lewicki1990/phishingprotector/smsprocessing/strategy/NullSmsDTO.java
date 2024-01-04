package com.github.lewicki1990.phishingprotector.smsprocessing.strategy;

import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsDTO;

public class NullSmsDTO extends SmsDTO {
    public NullSmsDTO() {
        super(0,0,0,null);
    }
}