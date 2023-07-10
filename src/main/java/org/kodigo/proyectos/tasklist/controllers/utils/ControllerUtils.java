package org.kodigo.proyectos.tasklist.controllers.utils;

import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.kodigo.proyectos.tasklist.security.jwt.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class ControllerUtils {
  @Autowired private UserRepository userRepository;
  @Autowired private JwtUtils jwtUtils;

  public UserEntity getUserEntityFromHeaders(HttpHeaders headers) {
    String token = headers.getFirst("Authorization").substring(7);
    String username = jwtUtils.getUsernameFromToken(token);
    return userRepository.findByUsername(username).orElseThrow();
  }
}
