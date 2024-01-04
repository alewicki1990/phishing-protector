package com.github.lewicki1990.phishingprotector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhishingProtectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhishingProtectorApplication.class, args);
    }
}
