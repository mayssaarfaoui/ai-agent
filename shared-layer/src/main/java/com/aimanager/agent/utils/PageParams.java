package com.aimanager.agent.utils;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ParameterObject
@Getter
@Setter
public class PageParams {

    @Parameter(required = false, description = "Page number. BY default, we get the first page.")
    private int pageNumber = 0;

    @Parameter(required = false, description = "Page size. By default, we get 10 records.")
    private int pageSize = 10;

    public Pageable getPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }


}
