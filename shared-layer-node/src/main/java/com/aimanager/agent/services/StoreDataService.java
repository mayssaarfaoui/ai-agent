package com.aimanager.agent.services;

import com.aimanager.agent.models.CFData;
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.repository.CFDataRepository;
import com.aimanager.agent.request.FetchedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StoreDataService<F extends Fetchable> {

    private final Logger log = LoggerFactory.getLogger(StoreDataService.class);

    private Map<Long, List<CFData>> memory = new HashMap<>();

    @Autowired
    CFDataRepository cfDataRepository;

    @Autowired
    FetchableConverter converter;

    public CFData convertToCFData(FetchedData form) {
        CFData cf = new CFData();
        return CFData.copy(form, cf);
    }

    public void storeFetchedData(FetchedData form) {
        CFData cf = convertToCFData(form);
        cfDataRepository.save(cf);
    }

    public void addData(List<CFData> data) {
        Long nodeId = data.get(0).getId().getNode();
        if (memory.containsKey(nodeId)) {
            memory.get(nodeId).addAll(data);
        } else {
            memory.put(nodeId, data);
        }
    }

    public void storeFetchedData(List<FetchedData> items) {
        if (items != null && !items.isEmpty()) {
            List<CFData> cfs = items.stream().map(this::convertToCFData).collect(Collectors.toList());
            cfDataRepository.saveAll(cfs);
            addData(cfs);
        }
    }

    public List<Object> getStoredData(Long nodeId) {
        List<CFData> cdata = cfDataRepository.findById_Node(nodeId);
        if (cdata == null || cdata.isEmpty())
            cdata = memory.get(nodeId);
        List<Object> items = converter.of(cdata);
        return items;
    }

    public void deleteStoredData(Long nodeId) {
        //cfDataRepository.deleteById_Node(nodeId);
        if (memory.containsKey(nodeId))
            memory.remove(nodeId);
    }
}
