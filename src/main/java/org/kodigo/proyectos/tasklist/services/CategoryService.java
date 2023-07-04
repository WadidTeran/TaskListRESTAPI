package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

  @Autowired private CategoryRepository categoryRepository;
  @Autowired private UserRepository userRepository;

  public boolean createCategory(User user, Category category) {
    if (!existCategory(user, category.getName())
        && category.getName() != null
        && !category.getName().isBlank()
        && !category.getName().isEmpty()) {
      category.setUser(user);
      categoryRepository.save(category);
      return true;
    } else {
      return false;
    }
  }

  public boolean modifyCategory(Category category) {
    if (categoryRepository.existsById(category.getCategoryId())
        && category.getName() != null
        && !category.getName().isBlank()
        && !category.getName().isEmpty()) {
      categoryRepository.save(category);
      return true;
    } else {
      return false;
    }
  }

  public boolean deleteCategory(User user, Category category) {
    if (existCategory(user, category.getName())) {
      // TODO: Set the category_id field for each task in this category to null
      categoryRepository.delete(category);
      return true;
    } else {
      return false;
    }
  }

  public Optional<Category> getCategoryByName(User user, String categoryName) {
    return categoryRepository.findByUserAndName(user, categoryName);
  }

  public List<Category> getAllCategories(User user) {
    return categoryRepository.findByUser(user);
  }

  public boolean existCategory(User user, String categoryName) {
    Optional<Category> optionalCategory = categoryRepository.findByUserAndName(user, categoryName);
    return optionalCategory.isPresent();
  }
}
