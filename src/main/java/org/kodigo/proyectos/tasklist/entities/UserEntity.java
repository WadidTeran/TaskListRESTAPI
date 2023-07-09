package org.kodigo.proyectos.tasklist.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @NotBlank
  @NotNull
  @Size(max = 30)
  @Column(unique = true, nullable = false)
  private String username;

  @Email(regexp = "^[A-Za-z0-9+_.-]+@[a-z]+\\.(co|com)$")
  @NotBlank
  @NotNull
  @Size(max = 80)
  @Column(unique = true, nullable = false)
  private String email;

  @NotBlank
  @NotNull
  @Length(min = 8)
  @Column(nullable = false)
  private String password;

  @JsonIgnore
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<Category> categories;

  @JsonIgnore
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<Task> tasks;

  @Override
  public String toString() {
    return "UserEntity{"
        + "userId="
        + userId
        + ", username='"
        + username
        + '\''
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + '}';
  }
}
