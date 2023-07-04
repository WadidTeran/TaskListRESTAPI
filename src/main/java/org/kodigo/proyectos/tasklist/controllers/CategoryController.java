package org.kodigo.proyectos.tasklist.controllers;

import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired private CategoryService categoryService;

  @DeleteMapping
  public ResponseEntity<?> deleteAllCategory(@RequestBody User user) {
    if (categoryService.deleteAllCategories(user)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{categoryName}")
  public ResponseEntity<?> deleteCategory(
      @RequestBody User user, @PathVariable String categoryName) {
    if (categoryService.deleteCategory(user, categoryName)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
