package com.aimanager.agent.services;

import com.aimanager.agent.form.ServiceRequestForm;
import com.aimanager.agent.models.FetchableType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class HttpService {


    private static Logger logger = LoggerFactory.getLogger(HttpService.class);

    private RestTemplate restTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public HttpService() {
        this.restTemplate = new RestTemplate();
    }


    public ResponseEntity<String> fetchData(FetchableType fetchableType, ServiceRequestForm requestForm) {

        // Build the URL with query parameters
        URI url = buildUrlWithParams(fetchableType, requestForm.getUrl(), requestForm.getQueryParams());
        logger.info("Fetching data from URL: {}", url);

        // Create the headers
        HttpHeaders headers = new HttpHeaders();
        requestForm.getHeaders().forEach(headers::set);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        // Create the HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send the GET request and get the response
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        // Return the response body
        return response;

    }

    private URI buildFetchGenericUrl(String url, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        queryParams.forEach((key, value) -> {
            // logger.info("Adding query param key: {} value: {}", key, value);
            // Do NOT manually URLEncode the JSON
            builder.queryParam(key, value);
        });

        return builder
                .build(false)
                .toUri(); // ✅ NO encoding here
    }

    // #{"creatorId":{"filters": [{"compOp":"eq","value":590782}]},"prState":{"filters":[{"compOp":"eq","value":"open"}]}}

    public String buildFetchPRFilters(Long creatorId) {
        String filtersJson =
                "{"
                        + "\"creatorId\":{\"filters\":[{\"compOp\":\"eq\",\"value\":" + creatorId + "}]},"
                        + "\"prState\":{\"filters\":[{\"compOp\":\"eq\",\"value\":\"open\"}]}"
                        + "}";
        return filtersJson;
    }

    private URI buildFetchPrUrl(String baseUrl, Map<String, String> queryParams) {

        try {
            Long creatorId = Long.parseLong(queryParams.get("userId"));

            String filterJson = buildFetchPRFilters(creatorId);

            // adjust query params
            queryParams.remove("userId");
            queryParams.put("filters", filterJson);

            return buildFetchGenericUrl(baseUrl, queryParams);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to build URL", e);
        }
    }


    private URI buildUrlWithParams(FetchableType fetchableType, String url, Map<String, String> queryParams) {
        if (fetchableType == FetchableType.FETCH_PR)
            return buildFetchPrUrl(url, queryParams);
        else
            return buildFetchGenericUrl(url, queryParams);
    }
}
