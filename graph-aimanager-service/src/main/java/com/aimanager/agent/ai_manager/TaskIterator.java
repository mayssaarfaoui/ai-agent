package com.aimanager.agent.ai_manager;

/*
  The TaskIterator class will be used to iterate through the tasks
  in the task list.
  The manager will use this class to iterate through the tasks and
  check on the status of each task.
 */

import java.util.List;

public class TaskIterator {
  private List<Task> tasks;

  public TaskIterator(List<Task> tasks) { //fixme this should take a user id and fetch the tasks
    this.tasks = tasks;
  }
  /*
  Get the user's tasks from the task server API
   */
  public List<Task> getTasksFromAPI() {
    List<Task> tasks = null;
    System.out.println("Getting tasks from the server...");
    return tasks;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public void iterateTasks() {
    for (Task task : tasks) {
      System.out.println("Task: " + task.getTitle());
      System.out.println("Description: " + task.getDescription());
      System.out.println("Status: " + task.getTaskStatus());
    }
  }
}
