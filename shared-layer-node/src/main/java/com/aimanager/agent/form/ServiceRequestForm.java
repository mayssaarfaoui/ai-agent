package com.aimanager.agent.form;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ServiceRequestForm {

    private String url;

    private Map<String, String> headers = new HashMap<>();

    private Map<String, String> queryParams = new HashMap<>();

    @Override
    public String toString() {
        return "ServiceRequestForm{" +
                "url='" + url + '\'' +
                ", headers=" + headers +
                ", queryParams=" + queryParams +
                '}';
    }

    public void addQueryParams(Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (this.queryParams.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("Query parameter '" + entry.getKey() + "' already exists");
            }
        }
        this.queryParams.putAll(params);
    }

    public void addQueryParameter(String parameterKey, String parameterValue) {
        if (this.queryParams.containsKey(parameterKey)) {
            throw new IllegalArgumentException("Query parameter '" + parameterKey + "' already exists");
        }
        this.queryParams.put(parameterKey, parameterValue);
    }

    public void setQueryParameter(String parameterKey, String parameterValue) {
        if (this.queryParams.containsKey(parameterKey))
            this.queryParams.remove(parameterKey);
        this.queryParams.put(parameterKey, parameterValue);
    }
}
