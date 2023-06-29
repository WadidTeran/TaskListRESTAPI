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

  public boolean createCategory(Category category) {
    if (existUser(category.getUser()) && !existCategory(category)) {
      category.setUser(userRepository.findById(category.getUser().getUserId()).get());
      categoryRepository.save(category);
      return true;
    } else {
      return false;
    }
  }

  public boolean modifyCategory(Category category) {
    if (existCategory(category)) {
      categoryRepository.save(category);
      return true;

    } else {
      return false;
    }
  }

  public boolean deleteCategory(Category category) {
    if (existCategory(category)) {
      categoryRepository.delete(category);
      return true;
    } else {
      return false;
    }
  }

  public Optional<Category> getCategoryByName(User user, String name) {
    return categoryRepository.findByUserAndName(user, name);
  }

  public List<Category> getAllCategories(User user) {
    return categoryRepository.findByUser(user);
  }

  private boolean existCategory(Category category) {
    Optional<Category> optionalCategory =
        categoryRepository.findByUserAndName(category.getUser(), category.getName());
    return optionalCategory.isPresent();
  }

  private boolean existUser(User user) {
    Optional<User> optionalUser = userRepository.findById(user.getUserId());
    return optionalUser.isPresent();
  }
}
