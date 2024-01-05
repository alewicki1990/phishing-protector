package com.github.lewicki1990.phishingprotector.phishingchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class PhishingCheckerService {

    private final String endpoint;
    private final String authToken;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public PhishingCheckerService( @Value("${phishing-checker.endpoint}") String endpoint, @Value("${phishing-checker.token}")String authToken, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.endpoint = endpoint;
        this.authToken = authToken;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public boolean isUrlPhishing(String suspiciousUrl) {
        HttpEntity<String> requestEntity = getHttpEntity(suspiciousUrl);
        if (requestEntity == null){
            return false;
        }

        ResponseEntity<String> responseEntity;

            responseEntity = sendPostRequestToPhishingCheckingEndpoint(requestEntity);


        PhishingCheckerResponse phishingCheckerResponse = getPhishingCheckerResponse(suspiciousUrl, responseEntity);

        if(phishingCheckerResponse == null){
            return false;
        }
        return !phishingCheckerResponse.isSafe();
    }
    private ResponseEntity<String> sendPostRequestToPhishingCheckingEndpoint(HttpEntity<String> entity) {
        try {
            return restTemplate.postForEntity(endpoint, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {

            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    private PhishingCheckerResponse getPhishingCheckerResponse(String suspiciousUrl, ResponseEntity<String> responseEntity) {

        PhishingCheckerResponse phishingCheckerResponse;

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            try {
                phishingCheckerResponse = objectMapper.readValue(responseEntity.getBody(), PhishingCheckerResponse.class);
            } catch (Exception e) {
                log.error("Error occurred while deserializing response for suspiciousUrl={}. Response:{}", suspiciousUrl, responseEntity);
                return null;
            }
        } else {
            log.error("Error occurred while evaluating for suspicious url: {}, status code {} ", suspiciousUrl, responseEntity.getStatusCode());
            return null;
        }
        return phishingCheckerResponse;
    }

    private HttpEntity<String> getHttpEntity(String suspiciousUrl) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(authToken);

        PhishingCheckerRequest request = new PhishingCheckerRequest(suspiciousUrl);
        HttpEntity<String> requestEntity;
        try {
            String requestBody = objectMapper.writeValueAsString(request);
            requestEntity = new HttpEntity<>(requestBody, requestHeaders);
            log.debug("Request headers: {}", requestEntity.getHeaders());
            log.debug("Request body: {}", requestEntity.getBody());
        } catch (Exception e) {
            log.error("Error occurred while serializing request for suspicious url:{}", suspiciousUrl);
            return null;
        }
        return requestEntity;
    }
}