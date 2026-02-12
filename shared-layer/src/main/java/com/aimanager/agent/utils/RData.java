package com.aimanager.agent.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class RData<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    public static <T> RData<T> of(Page<T> page) {
        RData<T> data = new RData<>();
        data.setContent(page.getContent());
        data.setTotalElements(page.getTotalElements());
        data.setTotalPages(page.getTotalPages());
        data.setPageNumber(page.getNumber() + 1); // Convert to 1-based index
        data.setPageSize(page.getSize());
        return data;
    }
}