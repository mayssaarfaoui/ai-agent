package com.aimanager.agent.form;

import java.util.HashMap;
import java.util.Map;

import com.aimanager.agent.request.FetchedDataType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestForm {

    private Map<String, String> queryParams;

    public String toString() {
        String str = "RequestForm{ queryParams={";
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            str += entry.getKey() + "=" + entry.getValue() + ", ";
        }
        str += "}}";
        return str;
    }

    public void addQueryParam(String key, String value) {
        if(queryParams == null)
            queryParams = new HashMap<>();
        this.queryParams.put(key, value);
    }

}
