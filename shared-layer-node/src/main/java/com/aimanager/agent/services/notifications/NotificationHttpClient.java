package com.aimanager.agent.services.notifications;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NotificationHttpClient {

    private final RestTemplate restTemplate;
    private final String notificationBaseUrl;
    private final String authToken;

    public NotificationHttpClient(
            RestTemplate restTemplate,
            String notificationBaseUrl,
            String authToken) {

        this.restTemplate = restTemplate;
        this.notificationBaseUrl = notificationBaseUrl;
        this.authToken = authToken;
    }

    public <T> ResponseEntity<Void> sendNotification(
            String eventType,
            String channel,
            String itemId,
            T item,
            List<Long> receiversIds) {

        // Build URL (NO manual encoding!)
        URI uri = UriComponentsBuilder
                .fromHttpUrl(notificationBaseUrl)
                .queryParam("channel", channel)
                .queryParam("type", eventType)
                .build()
                .toUri();

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ArrayList accepts = new ArrayList<>();
        accepts.add(MediaType.ALL);
        headers.setAccept(accepts);
        headers.set("teamboost-api-version", "1.0.0");
        headers.set("server-name", "TEAMBOOST_EWS");
        headers.setBearerAuth(authToken);

        // Body
        NotificationRequest<T> body =
                new NotificationRequest<>(itemId, item, receiversIds);

        HttpEntity<NotificationRequest<T>> entity =
                new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                Void.class
        );
    }
}

