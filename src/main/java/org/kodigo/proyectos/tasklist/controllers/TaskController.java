package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
  @Autowired private TaskService taskService;

  @Operation(summary = "Gets a list of tasks according to query params")
  @GetMapping
  public ResponseEntity<List<Task>> getTaskList(
      @RequestBody UserEntity user,
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
    Optional<List<Task>> optTaskList = taskService.getTaskList(user, status, due, rel, category);
    if (optTaskList.isPresent()) {
      List<Task> tasks = optTaskList.get();
      return tasks.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tasks);
    }
    return ResponseEntity.badRequest().build();
  }
}
