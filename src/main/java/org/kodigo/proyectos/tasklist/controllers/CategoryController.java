package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired private CategoryService categoryService;

  @Operation(summary = "Get category by name")
  @GetMapping("/{categoryName}")
  public ResponseEntity<Category> getCategoryByName(
      @RequestBody UserEntity user, @PathVariable String categoryName) {
    Optional<Category> optionalCategory = categoryService.getCategoryByName(user, categoryName);
    return optionalCategory
        .map(category -> new ResponseEntity<>(category, HttpStatus.OK))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Get all categories of the user")
  @GetMapping
  public ResponseEntity<List<Category>> getCategories(@RequestBody UserEntity user) {
    List<Category> categories = categoryService.getAllCategories(user);
    if (!categories.isEmpty()) {
      return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Create category")
  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody Category category) {
    if (categoryService.createCategory(category)) {
      Category categoryToShow =
          categoryService.getCategoryByName(category.getUser(), category.getName()).orElseThrow();
      return new ResponseEntity<>(categoryToShow, HttpStatus.CREATED);
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Modify category")
  @PutMapping
  public ResponseEntity<Category> modifyCategory(@RequestBody Category category) {
    switch (categoryService.modifyCategory(category)) {
      case OK -> {
        Category categoryToShow =
            categoryService.getCategoryByName(category.getUser(), category.getName()).orElseThrow();
        return new ResponseEntity<>(categoryToShow, HttpStatus.OK);
      }
      case NOT_FOUND -> {
        return ResponseEntity.notFound().build();
      }
      default -> {
        return ResponseEntity.badRequest().build();
      }
    }
  }

  @Operation(summary = "Delete specific category by Id and sets to null all related tasks")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Category> deleteCategory(
      @RequestBody UserEntity user, @PathVariable Long categoryId) {
    if (categoryService.deleteCategory(user, categoryId)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Delete all categories of the user")
  @DeleteMapping
  public ResponseEntity<Category> deleteCategories(@RequestBody UserEntity user) {
    categoryService.deleteAllCategories(user);
    return ResponseEntity.noContent().build();
  }
}
