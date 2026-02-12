package com.aimanager.agent.models;

import com.aimanager.agent.request.FetchedDataType;

import java.util.Map;

public abstract class Fetchable {

    public abstract String getKey();

    public abstract Map<String,String> convertToParameters();

}
