package com.aimanager.agent.Form;

import com.aimanager.agent.enums.SendType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.NotNull;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@ParameterObject
@Getter
@Setter
public class CreateQuestionWithFFForm extends GraphForm {

    @Parameter(required = true, description = "The question text.")
    @NotNull(message = "Question text is required")
    private String questionText;

    @Parameter(
            required = true,
            description =
                    "The user's response can be sent to an external service.\n\n" +
                            "If you want to enable this behavior, provide the service details in the request body:\n\n" +
                            "- **sendTo**: The target service URL.\n" +
                            "- **sendType**: Defines how the response is sent. Allowed values:\n" +
                            "    - `SEND_AS_PARAMETER` → The response is appended as a query parameter.\n" +
                            "    - `SEND_AS_BODY` → The response is included in the request body.\n" +
                            "- **responseParameterName**: The name of the parameter that will hold the user's response. Required only when `SEND_AS_PARAMETER` is used.\n" +
                            "- **headers**: Optional service header parameters.\n" +
                            "  Example:\n" +
                            "  `{ \"Content-Type\": \"application/json\", \"X-Token\": \"abc123\" }`\n"
    )
    @NotNull(message = "Question text is required")
    private boolean sendResponse;

    /*@Parameter(required = false, description = "Response's send Type. " +
            "This field is required if you want to send the user's response to a specific service. " +
            "It depends on the required service specification.")
    @NotNull(message = "Question text is required")
    private SendType sendType;

    @Parameter(required = false, description = "The question text. "+
            "This field is required if you want to send the user's response to a specific service. " +
            "It depends on the required service specification.")
    @NotNull(message = "Question text is required")
    private String responseParameterName;

    @Parameter(required = false, description = "Service's url . "+
            "This field is required if you want to send the user's response to a specific service. " +
            "It depends on the required service specification."+
            "The response given by the user will be sent to the indicate service.")
    private String sendTo;

    @Schema(
            description = "Service's header parameters",
            example = "{\"Content-Type\": \"application/json\", \"X-Token\": \"abc123\"}",
            additionalPropertiesSchema = String.class
    )
    private Map<String, String> headers;*/
}
