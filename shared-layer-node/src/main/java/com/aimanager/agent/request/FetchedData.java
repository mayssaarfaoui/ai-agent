package com.aimanager.agent.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchedData {
    private Long nodeId;
    private String key;
    private String value;
}
