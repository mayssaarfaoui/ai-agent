package com.aimanager.agent.Form;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.aimanager.agent.models.FetchedResponseType;
import org.springdoc.api.annotations.ParameterObject;

import com.aimanager.agent.models.FetchableType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFetchDataNodeForm{

   /* @Parameter(required = true, description = "The type of the fetchable data")
    @NotNull(message = "Fetchable type is required")
    private FetchableType fetchableType;*/

    /*@Parameter(required = true, description = "The type of the fetched response.")
    @NotNull(message = "Fetched Response type is required")
    private FetchedResponseType fetchedResponseType;*/

    @NotBlank(message = "Service's URL is required")
    private String fetchServiceUrl;

    private Map<String, String> headers;

    private List<String> parameters;

}
