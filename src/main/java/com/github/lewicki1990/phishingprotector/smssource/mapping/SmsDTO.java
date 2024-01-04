package com.github.lewicki1990.phishingprotector.smssource.mapping;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class SmsDTO {

    private long id;

    private long sender;

    private long recipient;

    private String message;

}