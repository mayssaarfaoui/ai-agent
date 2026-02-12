package com.aimanager.agent.services;

import com.aimanager.agent.enums.SendType;
import com.aimanager.agent.models.QuestionWithFreeFormNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ResponseForwarder {

    private final RestTemplate restTemplate;

    public ResponseForwarder(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends the user response to an external service based on the given node configuration.
     *
     * @param node     The QuestionWithFreeFormNode containing service config
     * @param response The actual user response text
     */
    public void sendResponse(QuestionWithFreeFormNode node, String response) {
        if (!node.isSendResponse()) {
            log.info("[ResponseForwarder] Service sending disabled for this node. Skipping.");
            return;
        }

        if (node.getSendTo() == null || node.getSendTo().isEmpty()) {
            log.warn("[ResponseForwarder] sendTo URL is missing. Skipping.");
            return;
        }

        log.info("[ResponseForwarder] Sending response '{}' to {}", response, node.getSendTo());

        if (node.getSendType() == SendType.SEND_AS_PARAMETER) {
            sendAsParameter(node, response);
        } else if (node.getSendType() == SendType.SEND_AS_BODY) {
            sendAsBody(node, response);
        } else {
            log.error("[ResponseForwarder] Unknown SendType for node: {}", node.getSendType());
        }
    }

    // ------------------------------------------------------------------------
    // SEND AS PARAMETER
    // ------------------------------------------------------------------------
    private void sendAsParameter(QuestionWithFreeFormNode node, String response) {

        if (node.getResponseParameterName() == null || node.getResponseParameterName().isEmpty()) {
            throw new IllegalArgumentException("responseParameterName is required when SEND_AS_PARAMETER is used.");
        }

        // Build target URL
        String url = UriComponentsBuilder
                .fromHttpUrl(node.getSendTo())
                .queryParam(node.getResponseParameterName(), response)
                .toUriString();

        HttpHeaders httpHeaders = buildHeaders(node.getHeaders());
        HttpEntity<Void> request = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<String> result =
                    restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            log.info("[ResponseForwarder] Response sent successfully (PARAMETER). Status={}",
                    result.getStatusCode());

        } catch (Exception ex) {
            log.error("[ResponseForwarder] Failed to send response as parameter to {}", node.getSendTo(), ex);
        }
    }

    // ------------------------------------------------------------------------
    // SEND AS BODY
    // ------------------------------------------------------------------------
    private void sendAsBody(QuestionWithFreeFormNode node, String response) {

        HttpHeaders headers = buildHeaders(node.getHeaders());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("response", response);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> result =
                    restTemplate.exchange(node.getSendTo(), HttpMethod.POST, request, String.class);

            log.info("[ResponseForwarder] Response sent successfully (BODY). Status={}",
                    result.getStatusCode());

        } catch (Exception ex) {
            log.error("[ResponseForwarder] Failed to send response as body to {}", node.getSendTo(), ex);
        }
    }

    // ------------------------------------------------------------------------
    // Build HTTP headers
    // ------------------------------------------------------------------------
    private HttpHeaders buildHeaders(Map<String, String> headerMap) {
        HttpHeaders headers = new HttpHeaders();

        if (headerMap != null) {
            headerMap.forEach(headers::add);
        }

        return headers;
    }
}
