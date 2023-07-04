package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.dtos.UserDTO;
import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  public boolean createUser(User user) {
    // TODO: Validate user fields
    Optional<User> optionalUserEmail = userRepository.findByEmail(user.getEmail());
    Optional<User> optionalUsername = userRepository.findByUsername(user.getUsername());
    if (optionalUserEmail.isEmpty() && optionalUsername.isEmpty()) {
      userRepository.save(user);
      return true;
    } else {
      return false;
    }
  }

  public boolean modifyUser(User user) {
    // TODO: Validate user fields
    if (userRepository.existsById(user.getUserId())) {
      userRepository.save(user);
      return true;
    } else {
      return false;
    }
  }

  public boolean deleteUser(User user) {
    if (userRepository.existsById(user.getUserId())) {
      // TODO: Remove all tasks and categories that belong to this user
      userRepository.deleteById(user.getUserId());
      return true;
    } else {
      return false;
    }
  }

  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean validateUser(UserDTO userDTO) {
    Long userId = userDTO.getUserId();
    Optional<User> optUser = userRepository.findById(userId);
    if (optUser.isPresent()) {
      User user = optUser.get();
      return (user.getEmail().equals(userDTO.getEmail())
              || user.getUsername().equals(userDTO.getUsername()))
          && user.getPassword().equals(userDTO.getPassword());
    }
    return false;
  }
}
