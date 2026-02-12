package com.aimanager.agent.models;

import java.util.List;

public abstract class DataResponse<F extends Fetchable> {

    public abstract List<F> getFetchedData();

}
