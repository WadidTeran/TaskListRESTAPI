package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.kodigo.proyectos.tasklist.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

  @Autowired private CategoryRepository categoryRepository;
  @Autowired private TaskRepository taskRepository;
  @Autowired private UserService userService;

  public boolean createCategory(Category category) {
    if (userService.validateUser(category.getUser())
            && notExistCategory(category.getUser(), category.getName())
            && validateCategory(category)) {
      category.setUser(category.getUser());
      categoryRepository.save(category);
      return true;
    }
    return false;
  }

  public Response modifyCategory(Category category) {
    Optional<Category> optionalCategory = categoryRepository.findById(category.getCategoryId());
    if (optionalCategory.isEmpty() || userService.validateUser(category.getUser())) {
      return Response.NOT_FOUND;
    }
    if (validateCategory(category) && notExistCategory(category.getUser(), category.getName())) {
      optionalCategory.get().setName(category.getName());
      categoryRepository.save(optionalCategory.get());
      return Response.OK;
    }
    return Response.BAD_REQUEST;
  }

  public boolean deleteCategory(UserEntity user, Long categoryId) {
    Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
    if (categoryOptional.isPresent()
            && categoryRepository.findByUserAndName(user, categoryOptional.get().getName()).isPresent()
            && userService.validateUser(user)) {
      for (Task task : categoryOptional.get().getTasks()) {
        task.setCategory(null);
        taskRepository.save(task);
      }
      categoryRepository.deleteById(categoryOptional.get().getCategoryId());
      return true;
    }
    return false;
  }

  public boolean deleteAllCategories(UserEntity user) {
    if (userService.validateUser(user)) {
      user.setCategories(getAllCategories(user));
      for (Category category : user.getCategories()) {
        deleteCategory(user, category.getCategoryId());
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

  private boolean validateCategory(Category category) {
    return (category.getName() != null
            && !category.getName().isBlank()
            && !category.getName().isEmpty());
  }
}