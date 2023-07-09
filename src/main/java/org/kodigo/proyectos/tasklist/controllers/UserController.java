package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.kodigo.proyectos.tasklist.controllers.utils.ControllerUtils;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
  @Autowired private UserService userService;
  @Autowired private ControllerUtils controllerUtils;

  @GetMapping("/account")
  public ResponseEntity<UserEntity> getUserEntity(@RequestHeader HttpHeaders headers) {
    return ResponseEntity.ok(controllerUtils.getUserEntityFromHeaders(headers));
  }

  @Operation(summary = "Modify account by specific instructions")
  @PatchMapping("/account")
  public ResponseEntity<UserEntity> modifyAccount(
      @RequestHeader HttpHeaders headers, @RequestBody @Valid UserEntity user) {
    return (userService.modifyUser(controllerUtils.getUserEntityFromHeaders(headers), user))
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Deletes account and deletes related categories and tasks")
  @DeleteMapping("/account")
  public ResponseEntity<UserEntity> deleteAccount(@RequestHeader HttpHeaders headers) {
    userService.deleteUser(controllerUtils.getUserEntityFromHeaders(headers));
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
