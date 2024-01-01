package com.github.lewicki1990.phishingprotector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsDTO {

    private long id;

    private long sender;

    private long recipient;

    private String message;

}
