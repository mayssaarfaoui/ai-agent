package com.aimanager.agent.services;
/*
import com.aimanager.agent.models.CFData;
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.Task;
import com.aimanager.agent.models.User;
import com.aimanager.agent.request.FetchedDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchableConverter<F extends Fetchable> {

    private final Logger log = LoggerFactory.getLogger(FetchableConverter.class);

    private final Gson gson = new Gson();

    public Fetchable of(CFData cfData){
        return gson.fromJson(cfData.getValue(), Fetchable.class);
    }

    public List<Fetchable> of(List<CFData> items){
        return items.stream().map(this::of).collect(Collectors.toList());
    }
}*/
