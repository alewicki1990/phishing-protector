package com.github.lewicki1990.phishingprotector.antiphishingsubscription;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MessageContentAnalyzer {

    private final String regex;

    public MessageContentAnalyzer(@Value("${phishing-detection.url-detection-regex}") String regex) {
        this.regex = regex;
    }

    public Set<String> extractUrls(String text) {
        if (text == null) {
            log.error("Cannot extract urls from text because text is null");
            throw new IllegalArgumentException();
        }

        Set<String> urls = new HashSet<>();
        Pattern pattern = Pattern.compile(regex);
        for (String word : text.split(" ")) {
            Matcher matcher = pattern.matcher(word);
            while (matcher.find()) {
                urls.add(word);
            }
        }
        return urls;
    }
}