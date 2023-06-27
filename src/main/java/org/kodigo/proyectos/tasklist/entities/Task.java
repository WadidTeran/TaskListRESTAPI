package org.kodigo.proyectos.tasklist.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private Long taskId;

  private String name;
  private String description;

  @Enumerated(EnumType.STRING)
  private Relevance relevance;

  @Column(name = "completed_date")
  private LocalDate completedDate;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "specified_time")
  private LocalTime specifiedTime;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "repeat_config_id", referencedColumnName = "repeat_config_id")
  private RepeatConfig repeatConfig;

  public Task(
      String name,
      String description,
      Relevance relevance,
      LocalDate dueDate,
      LocalTime specifiedTime,
      RepeatConfig repeatConfig) {
    this.name = name;
    this.description = description;
    this.relevance = relevance;
    this.dueDate = dueDate;
    this.specifiedTime = specifiedTime;
    this.repeatConfig = repeatConfig;
  }
}
