package com.aimanager.agent.services;

import com.aimanager.agent.Form.ServiceDetailsForm;
import com.aimanager.agent.enums.SendType;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceDetailsValidator {

    public static List<String> validate(ServiceDetailsForm form) {
        List<String> errors = new ArrayList<>();

        // Validate object not null
        if (form == null) {
            errors.add("ServiceDetailsForm cannot be null.");
            return errors;
        }

        // -------------------------------
        // 1. Validate sendTo (required if sendResponse is enabled)
        // -------------------------------
        if (!StringUtils.hasText(form.getSendTo())) {
            errors.add("sendTo (service URL) is required.");
        } else if (!isValidUrl(form.getSendTo())) {
            errors.add("sendTo must be a valid URL (http:// or https://).");
        }

        // -------------------------------
        // 2. Validate sendType
        // -------------------------------
        if (form.getSendType() == null) {
            errors.add("sendType is required.");
        }

        // -------------------------------
        // 3. Validate request method
        // -------------------------------
       /* if (form.getRequestSendMethod() == null) {
            errors.add("requestSendMethod is required.");
        }*/

        // -------------------------------
        // 4. Validate SEND_AS_PARAMETER rules
        // -------------------------------
        if (form.getSendType() == SendType.SEND_AS_PARAMETER) {
            if (!StringUtils.hasText(form.getResponseParameterName())) {
                errors.add("responseParameterName is required when sendType = SEND_AS_PARAMETER.");
            }
        }

        // -------------------------------
        // 5. Validate headers
        // -------------------------------
        if (form.getHeaders() != null) {
            for (Map.Entry<String, String> entry : form.getHeaders().entrySet()) {
                if (!StringUtils.hasText(entry.getKey())) {
                    errors.add("Header keys must not be empty.");
                }
                if (!StringUtils.hasText(entry.getValue())) {
                    errors.add("Header values must not be empty for key: " + entry.getKey());
                }
            }
        }

        return errors;
    }

    // Utility method to validate proper URL format
    private static boolean isValidUrl(String url) {
        try {
            new URL(url); // Attempt to parse URL
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
