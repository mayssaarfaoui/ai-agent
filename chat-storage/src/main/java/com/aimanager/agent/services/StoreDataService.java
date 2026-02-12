package com.aimanager.agent.services;

import com.aimanager.agent.models.CFData;
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.repository.CFDataRepository;
import com.aimanager.agent.request.FetchedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/*
@Service
public class StoreDataService<F extends Fetchable> {

    private final Logger log = LoggerFactory.getLogger(StoreDataService.class);

    @Autowired
    CFDataRepository cfDataRepository;

    @Autowired
    FetchableConverter converter;

    public CFData convertToCFData(FetchedData form){
        CFData cf = new CFData();
        return CFData.copy(form,cf);
    }

    public void storeFetchedData(FetchedData form){
        CFData cf = convertToCFData(form);
        cfDataRepository.save(cf);
    }

    public void storeFetchedData(List<FetchedData> items){
        if(items == null || items.isEmpty())
            throw new IllegalArgumentException("At last one elements should be provided");
        List<CFData> cfs = items.stream().map(this::convertToCFData).collect(Collectors.toList());
        cfDataRepository.saveAll(cfs);
    }

    public List<F> getStoredData(Long nodeId){
        log.info("Fetch data from Cassandra with node id : {}",nodeId);
        List<CFData>  cdata =  cfDataRepository.findById_Node(nodeId);
        List<F> items = converter.of(cdata);
        return items;
    }
}*/
