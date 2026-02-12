package com.aimanager.agent.models;

public enum FetchedResponseType {
    LIST_ITEMS(FetchedResponseType.Aliases.LIST_ITEMS),
    SINGLE_ITEM(FetchedResponseType.Aliases.SINGLE_ITEM),
    PAGINATED_RESPONSE(FetchedResponseType.Aliases.PAGINATED_RESPONSE);

    private final String type;

    FetchedResponseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public String getName() {
        return getType();
    }

    public static class Aliases {
        public static final String LIST_ITEMS = "LIST_ITEMS";
        public static final String SINGLE_ITEM = "SINGLE_ITEM";
        public static final String PAGINATED_RESPONSE = "PAGINATED_RESPONSE";
    }
}
