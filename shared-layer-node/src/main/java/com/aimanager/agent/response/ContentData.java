package com.aimanager.agent.response;

import com.aimanager.agent.models.Fetchable;
import com.aimanager.agent.models.Task;
import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ContentData {

    private JsonArray content;
    private int totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private int nextPage;
    private int previousPage;

    @Override
    public String toString() {
        return "ContentData{" +
                "content=" + content +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", nextPage=" + nextPage +
                ", previousPage=" + previousPage +
                '}';
    }
}