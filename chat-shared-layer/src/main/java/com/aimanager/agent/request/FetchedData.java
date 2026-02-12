package com.aimanager.agent.request;

import com.aimanager.agent.models.Fetchable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchedData {
    private Long nodeId;
    private FetchedDataType type;
    private String key;
    private String value;
}
