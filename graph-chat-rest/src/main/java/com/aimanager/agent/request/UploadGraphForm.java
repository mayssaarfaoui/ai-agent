package com.aimanager.agent.request;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ParameterObject
public class UploadGraphForm {

    @Parameter(required = true, description = "The graph id")
    @NotNull(message = "Graph id is required")
    private Long graphId;

    @Parameter(required = true, description = "The commit id")
    @NotNull(message = "Commit id is required")
    private Long commitId;

    MultipartFile file;
}
