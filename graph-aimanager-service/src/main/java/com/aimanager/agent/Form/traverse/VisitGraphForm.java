package com.aimanager.agent.Form.traverse;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;

import com.aimanager.agent.ai_manager.Task;
import com.aimanager.agent.enums.ScheduleStatus;
import com.aimanager.agent.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

@ParameterObject
@Getter
@Setter
public class VisitGraphForm {

   @Parameter(required = true, description = "The graph's id")
   @NotNull(message = "The graph's id is required")
   private Long graphId;

  /* @Parameter(description = "The node's title")
   private String title;

   @Parameter(description = "The node's description")
   private String description;

   @Parameter(description = "The node's task status")
   private TaskStatus taskStatus;

   @Parameter(description = "The node's schedule status")
   private ScheduleStatus scheduleStatus;


   public Task toTask() {
      return new Task(title, description, taskStatus, scheduleStatus);
   }*/

}
