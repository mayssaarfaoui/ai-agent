package com.aimanager.agent.Form;

import com.aimanager.agent.models.DownStreamType;
import com.aimanager.agent.models.IteratorType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public class CreateDownStreamNodeForm extends GraphForm{

    @Parameter(required = true, description = "The type of the DownStream node")
    @NotNull(message = "DownStream node's type is required")
    private DownStreamType downStreamType;

}
