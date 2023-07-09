package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.kodigo.proyectos.tasklist.controllers.utils.ControllerUtils;
import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired private CategoryService categoryService;
  @Autowired private ControllerUtils controllerUtils;

  @Operation(summary = "Get user categories")
  @GetMapping
  public ResponseEntity<List<Category>> getCategories(@RequestHeader HttpHeaders headers) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    List<Category> categories = userEntity.getCategories();
    return (!categories.isEmpty())
        ? ResponseEntity.ok(categories)
        : ResponseEntity.notFound().build();
  }

  @Operation(summary = "Create category")
  @PostMapping
  public ResponseEntity<Category> createCategory(
      @RequestHeader HttpHeaders headers, @RequestBody @Valid Category category) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    return (categoryService.createCategory(userEntity, category))
        ? ResponseEntity.created(URI.create("/" + category.getCategoryId())).build()
        : ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Modify category")
  @PutMapping
  public ResponseEntity<Category> modifyCategory(
      @RequestHeader HttpHeaders headers, @RequestBody @Valid Category category) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    switch (categoryService.modifyCategory(userEntity, category)) {
      case OK -> {
        return ResponseEntity.ok().build();
      }
      case NOT_FOUND -> {
        return ResponseEntity.notFound().build();
      }
      default -> {
        return ResponseEntity.badRequest().build();
      }
    }
  }

  @Operation(summary = "Delete all categories of the user")
  @DeleteMapping
  public ResponseEntity<Category> deleteCategories(@RequestHeader HttpHeaders headers) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    return (categoryService.deleteAllCategories(userEntity))
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @Operation(summary = "Get category by id")
  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategoryById(
      @RequestHeader HttpHeaders headers, @PathVariable Long categoryId) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    Optional<Category> optionalCategory = categoryService.getCategoryById(userEntity, categoryId);
    return optionalCategory
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Delete specific category by id and sets to null all related tasks")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Category> deleteCategory(
      @RequestHeader HttpHeaders headers, @PathVariable Long categoryId) {
    UserEntity userEntity = controllerUtils.getUserEntityFromHeaders(headers);
    return (categoryService.deleteCategory(userEntity, categoryId))
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
