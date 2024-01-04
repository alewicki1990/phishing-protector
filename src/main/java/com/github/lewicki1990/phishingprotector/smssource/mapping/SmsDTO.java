package com.github.lewicki1990.phishingprotector.smssource.mapping;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PUBLIC)
public class SmsDTO {

    private long id;

    private long sender;

    private long recipient;

    private String message;

}