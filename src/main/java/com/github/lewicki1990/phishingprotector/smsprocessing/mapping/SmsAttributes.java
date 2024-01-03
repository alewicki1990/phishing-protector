package com.github.lewicki1990.phishingprotector.smsprocessing.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsAttributes {
    private String sender;

    private String recipient;

    private String message;
}