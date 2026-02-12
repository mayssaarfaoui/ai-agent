package com.aimanager.agent.Form;

import com.aimanager.agent.enums.SendType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ServiceDetailsForm {
    private String sendTo;
    private SendType sendType;
    private String responseParameterName;
    private Map<String, String> headers;
}
