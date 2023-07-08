package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.kodigo.proyectos.tasklist.utils.CredentialsValidator.isValidEmail;
import static org.kodigo.proyectos.tasklist.utils.CredentialsValidator.isValidPassword;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  public boolean createUser(UserEntity user) {
    if (user.getUserId() == null && validateUserFields(user)) {
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean modifyUser(UserEntity newUser) {
    Optional<UserEntity> optUser = userRepository.findById(newUser.getUserId());
    if (optUser.isPresent() && validateUserFields(newUser)) {
      setChanges(optUser.get(), newUser);
      userRepository.save(optUser.get());
      return true;
    }
    return false;
  }

  public boolean deleteUser(UserEntity user) {
    if (validateUser(user)) {
      userRepository.delete(user);
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
    if (user.getEmail() != null) {
      Optional<UserEntity> optUserByEmail = userRepository.findByEmail(user.getEmail());
      if (!isValidEmail(user.getEmail())
          || (optUserByEmail.isPresent()
              && !optUserByEmail.get().getUserId().equals(user.getUserId()))) {
        return false;
      }
    }
    if (user.getUsername() != null) {
      Optional<UserEntity> optUserByName = userRepository.findByUsername(user.getUsername());
      if (optUserByName.isPresent() && !optUserByName.get().getUserId().equals(user.getUserId())) {
        return false;
      }
    }
    return user.getPassword() == null || isValidPassword(user.getPassword());
  }

  private void setChanges(UserEntity oldUser, UserEntity newUser) {
    if (newUser.getUsername() != null && !oldUser.getUsername().equals(newUser.getUsername())) {
      oldUser.setUsername(newUser.getUsername());
    }
    if (newUser.getEmail() != null && !oldUser.getEmail().equals(newUser.getEmail())) {
      oldUser.setEmail(newUser.getEmail());
    }
    if (newUser.getPassword() != null && !oldUser.getPassword().equals(newUser.getPassword())) {
      oldUser.setPassword(newUser.getPassword());
    }
  }
}
