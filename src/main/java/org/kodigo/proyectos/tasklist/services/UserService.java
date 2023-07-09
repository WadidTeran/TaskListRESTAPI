package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;
  @Autowired private CategoryService categoryService;
  @Autowired private PasswordEncoder passwordEncoder;

  public boolean registerUser(UserEntity user) {
    if (userDoesNotExist(user)) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      saveUser(user);
      return true;
    }
    return false;
  }

  public boolean modifyUser(UserEntity oldUser, UserEntity newUser) {
    if ((newUser.getUserId() == null)
        || (!oldUser.getUserId().equals(newUser.getUserId()))
        || (!oldUser.getUsername().equals(newUser.getUsername())
            && getUserByUsername(newUser.getUsername()).isPresent())
        || (!oldUser.getEmail().equals(newUser.getEmail())
            && getUserByEmail(newUser.getEmail()).isPresent())) return false;

    saveUser(newUser);
    return true;
  }

  public void saveUser(UserEntity user) {
    userRepository.save(user);
  }

  public void deleteUser(UserEntity user) {
    categoryService.deleteAllCategories(user);
    userRepository.delete(user);
  }

  public Optional<UserEntity> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<UserEntity> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean userDoesNotExist(UserEntity userEntity) {
    return getUserByUsername(userEntity.getUsername()).isEmpty()
        && getUserByEmail(userEntity.getEmail()).isEmpty();
  }
}
