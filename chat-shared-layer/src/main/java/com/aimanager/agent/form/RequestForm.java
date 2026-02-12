package com.aimanager.agent.form;

import java.util.HashMap;
import java.util.Map;

import com.aimanager.agent.request.FetchedDataType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestForm {
    
    private FetchedDataType type;

    private Map<String, String> queryParams;

}
