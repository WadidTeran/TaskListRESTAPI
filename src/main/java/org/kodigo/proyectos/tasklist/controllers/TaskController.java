package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.kodigo.proyectos.tasklist.controllers.utils.ControllerUtils;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin("http://127.0.0.1:5500")
@RequestMapping("/tasks")
public class TaskController {
  @Autowired private TaskService taskService;
  @Autowired private ControllerUtils controllerUtils;

  @Operation(summary = "Gets a list of tasks according to query params")
  @GetMapping
  public ResponseEntity<List<Task>> getTaskList(
      @RequestHeader HttpHeaders headers,
      @Parameter(description = "Task status query param: {completed, pending}") @RequestParam
          String status,
      @Parameter(description = "Task \"due to\" query param: {today, previous, future}")
          @RequestParam(required = false)
          String due,
      @Parameter(description = "Task relevance query param: {none, low, medium, high}")
          @RequestParam(required = false)
          String rel,
      @Parameter(description = "Task category name query param") @RequestParam(required = false)
          String category) {
    UserEntity user = controllerUtils.getUserEntityFromHeaders(headers);
    Optional<List<Task>> optTaskList = taskService.getTaskList(user, status, due, rel, category);
    if (optTaskList.isPresent()) {
      List<Task> tasks = optTaskList.get();
      return tasks.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tasks);
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Create task")
  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    boolean created = taskService.createTask(task);
    if (created) {
      return ResponseEntity.created(URI.create("/" + task.getName())).body(task);
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Modify task")
  @PutMapping
  public ResponseEntity<Task> modifyTask(@RequestBody Task task) {

    switch (taskService.modifyTask(task)) {
      case OK -> {
        return ResponseEntity.ok(task);
      }
      case BAD_REQUEST -> {
        return ResponseEntity.badRequest().build();
      }
      default -> {
        return ResponseEntity.notFound().build();
      }
    }
  }

  @Operation(summary = "Deletes all tasks")
  @DeleteMapping
  public ResponseEntity<Task> deleteTasks(@RequestHeader HttpHeaders headers) {
    UserEntity user = controllerUtils.getUserEntityFromHeaders(headers);
    if (taskService.deleteTasks(user)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Get task by id")
  @GetMapping("/{taskId}")
  public ResponseEntity<Task> getTaskById(
      @RequestBody UserEntity user, @PathVariable("taskId") Long taskId) {
    Optional<Task> taskOptional = taskService.findByUserAndTaskId(user, taskId);
    return taskOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Set completed date to now (completed) or null (pending)")
  @PatchMapping("/{taskId}")
  public ResponseEntity<Task> changeTaskStatus(
      @RequestBody UserEntity user, @PathVariable Long taskId) {
    if (taskService.changeStatus(user, taskId)) {
      Optional<Task> optTask = taskService.findByUserAndTaskId(user, taskId);
      return ResponseEntity.ok(optTask.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Deletes task by Id")
  @DeleteMapping("/{taskId}")
  public ResponseEntity<Task> deleteTask(@PathVariable Long taskId) {
    if (taskService.deleteTask(taskId)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
