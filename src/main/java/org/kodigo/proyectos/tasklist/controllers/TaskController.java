package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
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
      return tasks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tasks);
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Create task")
  @PostMapping
  public ResponseEntity<Task> createTask(
      @RequestHeader HttpHeaders headers, @RequestBody @Valid Task task) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    Task createdTask = taskService.createTask(userEntity, task);
    return (createdTask != null)
        ? ResponseEntity.created(URI.create("/" + task.getTaskId())).body(task)
        : ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Modify task")
  @PutMapping
  public ResponseEntity<Task> modifyTask(
      @RequestHeader HttpHeaders headers, @RequestBody @Valid Task task) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    switch (taskService.modifyTask(userEntity, task)) {
      case OK -> {
        return ResponseEntity.ok(
            taskService.findByUserAndTaskId(userEntity, task.getTaskId()).orElseThrow());
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
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    return (taskService.deleteTasks(userEntity))
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @Operation(summary = "Get task by id")
  @GetMapping("/{taskId}")
  public ResponseEntity<Task> getTaskById(
      @RequestHeader HttpHeaders headers, @PathVariable Long taskId) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    Optional<Task> taskOptional = taskService.findByUserAndTaskId(userEntity, taskId);
    return taskOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Set completed date to now (completed) or null (pending)")
  @PutMapping("/{taskId}")
  public ResponseEntity<Task> changeTaskStatus(
      @RequestHeader HttpHeaders headers, @PathVariable Long taskId) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    Task task = taskService.changeStatus(userEntity, taskId);
    return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
  }

  @Operation(summary = "Deletes task by Id")
  @DeleteMapping("/{taskId}")
  public ResponseEntity<Task> deleteTask(
      @RequestHeader HttpHeaders headers, @PathVariable Long taskId) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    return (taskService.deleteTask(userEntity, taskId))
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
