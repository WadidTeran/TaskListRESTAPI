package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

  @Autowired private CategoryRepository categoryRepository;
  @Autowired private TaskRepository taskRepository;
  @Autowired private UserRepository userRepository;

  public boolean createCategory(UserEntity user, Category category) {
    if (notExistCategory(user, category.getName())
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

  public boolean deleteCategory(UserEntity user, String categoryName) {
    Optional<Category> categoryOptional = categoryRepository.findByUserAndName(user, categoryName);
    if (categoryOptional.isPresent()) {
      Category category = categoryOptional.get();
      for (Task task : category.getTasks()) {
        task.setCategory(null);
        taskRepository.save(task);
      }
      categoryRepository.deleteById(category.getCategoryId());
      return true;
    }
    return false;
  }

  public boolean deleteAllCategories(UserEntity user) {
    Optional<UserEntity> optionalUser = userRepository.findById(user.getUserId());
    if (optionalUser.isPresent()) {
      for (Category category : optionalUser.get().getCategories()) {
        deleteCategory(user, category.getName());
      }
      return true;
    }
    return false;
  }

  public Optional<Category> getCategoryByName(UserEntity user, String categoryName) {
    return categoryRepository.findByUserAndName(user, categoryName);
  }

  public List<Category> getAllCategories(UserEntity user) {
    return categoryRepository.findByUser(user);
  }

  public boolean notExistCategory(UserEntity user, String categoryName) {
    Optional<Category> optionalCategory = categoryRepository.findByUserAndName(user, categoryName);
    return optionalCategory.isEmpty();
  }
}
