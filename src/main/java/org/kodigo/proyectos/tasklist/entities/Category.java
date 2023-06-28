package org.kodigo.proyectos.tasklist.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long categoryId;

  @Column(unique = true, nullable = false)
  private String name;

  @JsonIgnoreProperties(value = "categories")
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @JsonIgnoreProperties(value = "category")
  @OneToMany(mappedBy = "category")
  private List<Task> tasks;

  public Category(String name) {
    this.name = name;
  }
}
