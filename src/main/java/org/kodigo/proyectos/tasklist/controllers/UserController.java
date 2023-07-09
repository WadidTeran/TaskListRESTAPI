package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
  @Autowired private UserService userService;

  @Operation(summary = "Modify account by specific instructions")
  @PatchMapping("/account")
  public ResponseEntity<UserEntity> modifyAccount(@RequestBody @Valid UserEntity user) {
    userService.saveUser(user);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Delete specific account and delete related categories and tasks")
  @DeleteMapping("/account")
  public ResponseEntity<?> deleteAccount(@RequestBody @Valid UserEntity user) {
    userService.deleteUser(user);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Creates a new account")
  @PostMapping("/register")
  public ResponseEntity<UserEntity> register(@RequestBody @Valid UserEntity user) {
    if (userService.registerUser(user)) {
      return ResponseEntity.ok(user);
    }
    return ResponseEntity.badRequest().build();
  }
}
