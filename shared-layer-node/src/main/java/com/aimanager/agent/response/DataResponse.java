package com.aimanager.agent.response;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class DataResponse<F> {

    private String status;
    private String message;
    @JsonProperty("data")
    private ContentData data;

    public String getResponseStatus() {
        return status;
    }

    public boolean hasNoFetchedData() {
        return data == null || (data.getContent() == null || data.getContent().isEmpty());
    }

    public JsonArray getFetchedData() {
        return data.getContent();
    }

    @Override
    public String toString() {
        return "DataResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
