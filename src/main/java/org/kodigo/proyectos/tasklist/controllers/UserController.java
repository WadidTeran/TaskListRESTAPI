package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class UserController {
  @Autowired private UserService userService;

  @Operation(summary = "Modify account by specific instructions")
  @PatchMapping
  public ResponseEntity<UserEntity> modifyAccount(@RequestBody UserEntity user) {
    userService.saveUser(user);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Delete specific account and delete related categories and tasks")
  @DeleteMapping
  public ResponseEntity<?> deleteAccount(@RequestBody UserEntity user) {
    userService.deleteUser(user);
    return ResponseEntity.noContent().build();
  }
}
