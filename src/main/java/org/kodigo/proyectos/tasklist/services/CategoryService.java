package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

  @Autowired private CategoryRepository categoryRepository;
  @Autowired private TaskRepository taskRepository;

  public boolean createCategory(UserEntity userEntity, Category category) {
    if (notExistCategory(userEntity, category.getName())) {
      category.setUser(userEntity);
      categoryRepository.save(category);
      return true;
    }
    return false;
  }

  public HttpStatus modifyCategory(UserEntity userEntity, Category category) {
    if (category.getCategoryId() == null) return HttpStatus.BAD_REQUEST;

    Optional<Category> optionalCategory =
        categoryRepository.findByUserAndCategoryId(userEntity, category.getCategoryId());
    if (optionalCategory.isEmpty()) return HttpStatus.NOT_FOUND;

    if (notExistCategory(userEntity, category.getName())) {
      Category categoryToModify = optionalCategory.get();
      categoryToModify.setName(category.getName());
      categoryRepository.save(categoryToModify);
      return HttpStatus.OK;
    }
    return HttpStatus.BAD_REQUEST;
  }

  public boolean deleteCategory(UserEntity user, Long categoryId) {
    Optional<Category> categoryOptional = getCategoryById(user, categoryId);
    if (categoryOptional.isPresent()) {
      Category categoryToDelete = categoryOptional.get();
      setCategoryToNullForAllRelatedTasks(categoryToDelete);
      categoryRepository.delete(categoryToDelete);
      return true;
    }
    return false;
  }

  public boolean deleteAllCategories(UserEntity user) {
    List<Category> categories = user.getCategories();
    if (categories.isEmpty()) return false;

    for (Category category : categories) {
      setCategoryToNullForAllRelatedTasks(category);
      categoryRepository.deleteById(category.getCategoryId());
    }

    return true;
  }

  public Optional<Category> getCategoryByName(UserEntity user, String categoryName) {
    return categoryRepository.findByUserAndName(user, categoryName);
  }

  public Optional<Category> getCategoryById(UserEntity user, Long categoryId) {
    return categoryRepository.findByUserAndCategoryId(user, categoryId);
  }

  public boolean notExistCategory(UserEntity user, String categoryName) {
    Optional<Category> optionalCategory = categoryRepository.findByUserAndName(user, categoryName);
    return optionalCategory.isEmpty();
  }

  private void setCategoryToNullForAllRelatedTasks(Category category) {
    for (Task task : category.getTasks()) {
      task.setCategory(null);
      taskRepository.save(task);
    }
  }
}
