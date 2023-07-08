package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  public void saveUser(UserEntity user) {
    userRepository.save(user);
  }

  public void deleteUser(UserEntity user) {
    userRepository.delete(user);
  }

  public Optional<UserEntity> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<UserEntity> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
