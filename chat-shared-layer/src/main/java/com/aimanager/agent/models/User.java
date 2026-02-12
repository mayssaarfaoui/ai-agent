package com.aimanager.agent.models;

import com.aimanager.agent.request.FetchedDataType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class User implements Fetchable{

    private Long userId;
    private Long organizationId;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", organizationId=" + organizationId +
                '}';
    }

    @Override
    public String getKey() {
        return userId.toString();
    }

    @Override
    public Map<String, String> convertToParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("organization_id",organizationId.toString());
        return params;
    }
}
