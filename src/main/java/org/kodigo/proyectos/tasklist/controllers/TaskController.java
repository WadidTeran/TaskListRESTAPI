package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.services.TaskService;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
  private final Logger log = LoggerFactory.getLogger(TaskController.class);
  @Autowired private TaskService taskService;
  @Autowired private UserService userService;

  @Operation(summary = "Gets a list of tasks according to query params")
  @GetMapping
  public ResponseEntity<List<Task>> getTaskList(
      @RequestBody User user,
      @Parameter(description = "Task status query param: {completed, pending}") @RequestParam
          String status) {
    if (userService.validateUser(user)) {
      User userDB =
          (user.getEmail() != null)
              ? userService.getUserByEmail(user.getEmail()).orElseThrow()
              : userService.getUserByUsername(user.getUsername()).orElseThrow();

      List<Task> tasks;
      if (!status.equals("completed") && !status.equals("pending")) {
        log.debug("Bad value for query param status");
        return ResponseEntity.badRequest().build();
      }
      tasks =
          (status.equals("completed"))
              ? taskService.getAllCompletedTasks(userDB)
              : taskService.getAllPendingTasks(userDB);
      return tasks.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tasks);
    }
    return ResponseEntity.badRequest().build();
  }
}
