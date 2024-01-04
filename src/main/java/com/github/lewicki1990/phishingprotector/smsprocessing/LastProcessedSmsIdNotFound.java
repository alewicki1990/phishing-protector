package com.github.lewicki1990.phishingprotector.smsprocessing;

public class LastProcessedSmsIdNotFound extends RuntimeException {

    public LastProcessedSmsIdNotFound() {
    }

    public LastProcessedSmsIdNotFound(String message) {
        super(message);
    }
}
