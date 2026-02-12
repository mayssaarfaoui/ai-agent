package com.aimanager.agent.ai_manager;

import com.aimanager.agent.enums.ScheduleStatus;
import com.aimanager.agent.enums.TaskStatus;

public class Task {
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private ScheduleStatus scheduleStatus;

    public Task(String taskName, String taskDescription, TaskStatus taskStatus, ScheduleStatus scheduleStatus) {
        this.title = taskName;
        this.description = taskDescription;
        this.taskStatus = taskStatus;
        this.scheduleStatus = scheduleStatus;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public String getTaskStatusString() { return taskStatus.toString();}

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", scheduleStatus=" + scheduleStatus +
                '}';
    }
}