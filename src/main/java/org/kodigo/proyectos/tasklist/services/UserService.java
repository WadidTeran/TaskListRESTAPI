package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.UserEntity;
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

  public boolean createUser(UserEntity user) {
    if (user.getUserId() == null && thereAreNotEqualFields(user) && validateUserFields(user)) {
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean modifyUser(UserEntity user) {
    if (user.getUserId() != null
        && userRepository.existsById(user.getUserId())
        && thereAreNotEqualFields(user)
        && validateUserFields(user)) {
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean deleteUser(String userName) {
    Optional<UserEntity> optUser = userRepository.findByUsername(userName);
    if (optUser.isPresent()) {
      userRepository.delete(optUser.get());
      return true;
    }
    return false;
  }

  public Optional<UserEntity> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<UserEntity> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean validateUser(UserEntity userToValidate) {
    Optional<UserEntity> optUser = userRepository.findById(userToValidate.getUserId());
    if (optUser.isPresent()) {
      UserEntity user = optUser.get();
      return (user.getEmail().equals(userToValidate.getEmail())
              || user.getUsername().equals(userToValidate.getUsername()))
          && user.getPassword().equals(userToValidate.getPassword());
    }
    return false;
  }

  private boolean validateUserFields(UserEntity user) {
    return (isValidEmail(user.getEmail()) && isValidPassword(user.getPassword()));
  }

  private boolean thereAreNotEqualFields(UserEntity user) {
    Optional<UserEntity> optUserByName = userRepository.findByUsername(user.getUsername());
    Optional<UserEntity> optUserByEmail = userRepository.findByEmail(user.getEmail());
    return ((optUserByName.isEmpty() && optUserByEmail.isEmpty())
        || (optUserByName.isPresent() && optUserByName.get().getUserId().equals(user.getUserId()))
        || (optUserByEmail.isPresent()
            && optUserByEmail.get().getUserId().equals(user.getUserId())));
  }
}
