package com.aimanager.agent.controller;
/*
import com.aimanager.agent.models.CFData;
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.request.FetchedData;
import com.aimanager.agent.services.FetchableConverter;
import com.aimanager.agent.services.StoreDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class StoreDadaController {

    @Autowired
    StoreDataService storeDataService;

    @Autowired
    FetchableConverter converter;

    @PostMapping("/save")
    public ResponseEntity<String> storeFetchedData(@Validated @RequestBody FetchedData form, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            throw new IllegalArgumentException("Fetched Data form is not valid");

        storeDataService.storeFetchedData(form);
        return ResponseEntity.ok().body("Data stored successfully.");

    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> storeFetchedData(@RequestBody List<FetchedData> items){
        storeDataService.storeFetchedData(items);
        return ResponseEntity.ok().body("Data stored successfully.");

    }

    @GetMapping("/get")
    public ResponseEntity<List<Fetchable>> getStoredData(@RequestParam("Node id") Long nodeId){
        List<CFData> data = storeDataService.getStoredData(nodeId);
        List<Fetchable> items = converter.of(data);
        return ResponseEntity.ok().body(items);
    }
}*/
