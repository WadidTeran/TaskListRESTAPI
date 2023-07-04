package org.kodigo.proyectos.tasklist.dtos;

import lombok.Data;

@Data
public class UserDTO {
  private Long userId;
  private String username;
  private String email;
  private String password;
}
