package com.aimanager.agent.Form;

    import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;
import com.aimanager.agent.models.IteratorType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public class CreateIteratorNodeForm extends GraphForm{

    @Parameter(required = true, description = "The type of the iterator node")
    @NotNull(message = "Iterator node type is required")
    private IteratorType iteratorType;

}
