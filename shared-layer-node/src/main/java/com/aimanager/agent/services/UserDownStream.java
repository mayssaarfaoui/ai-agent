package com.aimanager.agent.services;

import com.aimanager.agent.form.RequestForm;
import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.User;
import com.aimanager.agent.nodes.DownStreamAgent;
import com.aimanager.agent.nodes.NodeContext;
import com.aimanager.agent.request.FetchedDataType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UserDownStream<T extends User> extends DownStreamAgent<Fetchable> {
    /**
     * Constructor to initialize the Downstream Node.
     *
     */
    public UserDownStream() {
        super();
    }

    public User convertData(Object data) {
        JsonObject obj = toJsonObject(data);

        User user = new User();

        if (obj.has("user") && obj.get("user").isJsonObject()) {
            JsonObject userObj = obj.getAsJsonObject("user");
            if (userObj.has("id")) {
                user.setUserId(userObj.get("id").getAsLong());
            }
        }

        if (obj.has("organization") && obj.get("organization").isJsonObject()) {
            JsonObject orgObj = obj.getAsJsonObject("organization");
            if (orgObj.has("id")) {
                user.setOrganizationId(orgObj.get("id").getAsLong());
            }
        }

        return user;
    }


    @Override
    public void process(Object data, GraphNode nextNode,NodeContext context) {
            User user = convertData(data);
            logger.info("Fetch PR related for a user : {}.", user.toString());
            NodeContext nc = buildNextNodeContext(user);
            Long graphId = context.get("graphId", Long.class);
            Long commitId = context.get("commitId", Long.class);
            nc.set("graphId", graphId);
            nc.set("commitId", commitId);
            nextNode.process(nc);
    }

    @Override
    public NodeContext buildNextNodeContext(Fetchable data) {
        User user = (User) data;
        Map<String,String> params = user.convertToParameters();
        RequestForm form = new RequestForm();
        form.setQueryParams(params);
        form.addQueryParam("userId", user.getUserId().toString());
        NodeContext nc =new NodeContext();
        nc.set("requestform",form);
        return nc;
    }
}
