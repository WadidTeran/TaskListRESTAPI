package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired private CategoryService categoryService;

  @Operation(summary = "Delete all categories and sets to null all related tasks")
  @DeleteMapping
  public ResponseEntity<Category> deleteAllCategory(@RequestBody UserEntity user) {
    if (categoryService.deleteAllCategories(user)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Delete specific category and sets to null all related tasks")
  @DeleteMapping("/{categoryName}")
  public ResponseEntity<Category> deleteCategory(
          @RequestBody UserEntity user, @PathVariable String categoryName) {
    if (categoryService.deleteCategory(user, categoryName)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
