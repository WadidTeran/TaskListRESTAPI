package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

  @Autowired private CategoryRepository categoryRepository;
  @Autowired private TaskRepository taskRepository;

  public boolean createCategory(Category category) {
    if (notExistCategory(category.getUser(), category.getName())) {
      categoryRepository.save(category);
      return true;
    }
    return false;
  }

  public HttpStatus modifyCategory(Category category) {
    if (category.getCategoryId() == null) return HttpStatus.BAD_REQUEST;

    Optional<Category> optionalCategory = categoryRepository.findById(category.getCategoryId());

    if (optionalCategory.isEmpty()) {
      return HttpStatus.NOT_FOUND;
    }
    if (notExistCategory(category.getUser(), category.getName())) {
      Category categoryToModify = optionalCategory.get();
      categoryToModify.setName(category.getName());
      categoryRepository.save(categoryToModify);
      return HttpStatus.OK;
    }
    return HttpStatus.BAD_REQUEST;
  }

  public boolean deleteCategory(UserEntity user, Long categoryId) {
    Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
    if (categoryOptional.isPresent()
        && categoryRepository
            .findByUserAndName(user, categoryOptional.get().getName())
            .isPresent()) {
      setCategoryToNullForAllRelatedTasks(categoryOptional.get());
      categoryRepository.deleteById(categoryOptional.get().getCategoryId());
      return true;
    }
    return false;
  }

  public void deleteAllCategories(UserEntity user) {
    List<Category> categories = user.getCategories();
    if (categories.isEmpty()) return;

    for (Category category : categories) {
      setCategoryToNullForAllRelatedTasks(category);
      categoryRepository.delete(category);
    }
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

  private void setCategoryToNullForAllRelatedTasks(Category category) {
    for (Task task : category.getTasks()) {
      task.setCategory(null);
      taskRepository.save(task);
    }
  }
}
