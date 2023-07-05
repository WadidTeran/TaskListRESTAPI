package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.kodigo.proyectos.tasklist.utils.CredentialsValidator.isValidEmail;
import static org.kodigo.proyectos.tasklist.utils.CredentialsValidator.isValidPassword;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;
  @Autowired private TaskRepository taskRepository;
  @Autowired private CategoryService categoryService;

  public boolean createUser(User user) {
    Optional<User> optionalUserEmail = userRepository.findByEmail(user.getEmail());
    Optional<User> optionalUsername = userRepository.findByUsername(user.getUsername());
    if (optionalUsername.isEmpty() && optionalUserEmail.isEmpty() && validateUserFields(user)) {
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean modifyUser(User user) {
    if (userRepository.existsById(user.getUserId())
        && thereAreNotEqualFields(user)
        && validateUserFields(user)) {
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean deleteUser(String userName) {
    Optional<User> optUser = userRepository.findByUsername(userName);
    if (optUser.isPresent()) {
      userRepository.delete(optUser.get());
      return true;
    }
    return false;
  }

  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean validateUser(User userToValidate) {
    Optional<User> optUser = userRepository.findById(userToValidate.getUserId());
    if (optUser.isPresent()) {
      User user = optUser.get();
      return (user.getEmail().equals(userToValidate.getEmail())
              || user.getUsername().equals(userToValidate.getUsername()))
          && user.getPassword().equals(userToValidate.getPassword());
    }
    return false;
  }

  private boolean validateUserFields(User user) {
    return (isValidEmail(user.getEmail()) && isValidPassword(user.getPassword()));
  }

  private boolean thereAreNotEqualFields(User user) {
    Optional<User> optUserByName = userRepository.findByUsername(user.getUsername());
    Optional<User> optUserByEmail = userRepository.findByEmail(user.getEmail());
    return ((optUserByName.isEmpty() && optUserByEmail.isEmpty())
        || (optUserByName.isPresent() && optUserByName.get().getUserId().equals(user.getUserId()))
        || (optUserByEmail.isPresent()
            && optUserByEmail.get().getUserId().equals(user.getUserId())));
  }
}
