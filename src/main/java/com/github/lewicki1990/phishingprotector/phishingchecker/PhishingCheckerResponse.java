package com.github.lewicki1990.phishingprotector.phishingchecker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhishingCheckerResponse {

    private boolean isSafe;

    private String threatType;

}