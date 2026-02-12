package com.aimanager.agent.services;

import com.aimanager.agent.form.ServiceRequestForm;
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.FetchableType;
import com.aimanager.agent.models.FetchedResponseType;
import com.aimanager.agent.nodes.FetchDataAgent;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.request.FetchedData;
import com.aimanager.agent.response.DataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchDataService<F extends Fetchable, R extends DataResponse> extends FetchDataAgent {

    protected final Logger log = LoggerFactory.getLogger(FetchDataService.class);

    protected final Gson gson = new Gson();

    @Autowired
    HttpService httpService;

    @Autowired
    StoreDataService storeDataService;

    public FetchDataService() {
        super();
    }

    public JsonArray getFetchedData(DataResponse response) {
        if (response.hasNoFetchedData())
            return new JsonArray();
        return response.getFetchedData();
    }

    public void checkFetchDataResponse(DataResponse data) {
        if (data == null || data.getStatus().equals("ERROR"))
            throw new IllegalArgumentException("Fetch data service is not available now Or wrong parameters was used.");
    }

    public DataResponse parseFetchedData(ResponseEntity<String> resp) {
        String json = resp.getBody();
        //log.info("Fetched data is : {}", json);
        DataResponse response = gson.fromJson(json, DataResponse.class);
        if (resp.getStatusCode().is2xxSuccessful()) {
            return response;
        } else {
            log.error("Error fetch data node  : {}", response.getMessage());
            throw new RestClientException(response.getMessage());
        }
    }

    /*public DataResponse parseFetchedData(ResponseEntity<DataResponse> resp) {
        if (resp.getStatusCode().is2xxSuccessful()) {
            return resp.getBody();
        } else {
            log.error("Error fetch data node  : {}", resp.getBody().getMessage());
            throw new RestClientException(resp.getBody().getMessage());
        }
    }*/

    private String generateKey(JsonElement item, String fallback) {

        if (item.isJsonObject()) {
            JsonObject obj = item.getAsJsonObject();

            if (obj.has("id")) {
                return obj.get("id").getAsString();
            }
            if (obj.has("uuid")) {
                return obj.get("uuid").getAsString();
            }
        }

        // Fallback: hash of JSON
        return DigestUtils.sha256Hex(fallback);
    }

    public FetchedData createFetchedData(JsonElement item) {
        FetchedData fd = new FetchedData();
        String jsonPayload = gson.toJson(item);
        fd.setNodeId(getNodeKey());
        fd.setKey(generateKey(item, jsonPayload));
        fd.setValue(jsonPayload);
        return fd;
    }

    public void saveFetchedData(JsonArray fetchedData) {
        List<FetchedData> items = new ArrayList<>();

        for (JsonElement element : fetchedData) {
            items.add(createFetchedData(element));
        }
        storeDataService.storeFetchedData(items);
        //httpService.storeFetchedData(agentMemoryServer,items);
    }

    /*@Override
    public void fetchData(NodeContext context,  FetchableType fetchableType, ServiceRequestForm form) {
        log.info("Node details : {}.", this.getNodeInfo());
        ResponseEntity<String> response = httpService.fetchData(fetchableType, form);
        DataResponse data = parseFetchedData(response);
        log.info(" returned Response :{}", data.toString());
        checkFetchDataResponse(data);
        JsonArray fetchedData = getFetchedData(data);
        log.info("fetched data : {}", fetchedData.toString());
        saveFetchedData(fetchedData);
    }*/

    public void fetchPaginatedData(NodeContext context, FetchableType fetchableType, ServiceRequestForm form) {
        log.info("Node details : {}.", this.getNodeInfo());
        form.addQueryParameter("size", "100");
        JsonArray fetchedData = new JsonArray();
        boolean moreData = true;
        int page = 1;
        while (moreData) {
            form.setQueryParameter("page", String.valueOf(page));
            ResponseEntity<String> response = httpService.fetchData(fetchableType, form);
            DataResponse data = parseFetchedData(response);
            //log.info(" returned Response :{}", data.toString());
            checkFetchDataResponse(data);
            JsonArray pageData = getFetchedData(data);
            fetchedData.addAll(pageData);
            moreData = data.getData().getPageNumber() == data.getData().getTotalPages() ? false : true;
            page = data.getData().getNextPage();
            //log.info("fetched data : {}", pageData.toString());
        }
        log.info("fetched data length : {}", fetchedData.size());
        saveFetchedData(fetchedData);
    }

    public void fetchRegularData(NodeContext context, FetchableType fetchableType, ServiceRequestForm form) {
        log.info("Node details : {}.", this.getNodeInfo());
        ResponseEntity<String> response = httpService.fetchData(fetchableType, form);
        DataResponse data = parseFetchedData(response);
        //log.info(" returned Response :{}", data.toString());
        checkFetchDataResponse(data);
        JsonArray fetchedData = getFetchedData(data);
        //log.info("fetched data : {}", fetchedData.toString());
        saveFetchedData(fetchedData);
    }

    @Override
    public void fetchData(NodeContext context, FetchableType fetchableType, FetchedResponseType fetchedResponseType, ServiceRequestForm form) {
        if (fetchedResponseType == FetchedResponseType.PAGINATED_RESPONSE)
            fetchPaginatedData(context, fetchableType, form);
        else
            fetchRegularData(context, fetchableType, form);
    }

    @Override
    public void execute(NodeContext context, FetchableType fetchableType, FetchedResponseType fetchedResponseType, ServiceRequestForm form) {
        log.info("Executing Fetch Data Node with ID : {} and type : {} using context : {}", getNodeKey(), fetchableType, context);
        if (fetchableType == null)
            throw new IllegalArgumentException("Fetchable type is required for fetch data node execution.");
        if (fetchedResponseType == null)
            throw new IllegalArgumentException("Fetched response type is required for fetch data node execution.");
        fetchData(context, fetchableType, fetchedResponseType, form);
    }

    @Override
    public NodeContext buildNextNodeContext(Fetchable data) {
        NodeContext nc = new NodeContext();
        nc.set("nodeId", this.getNodeKey());
        return nc;
    }
}

