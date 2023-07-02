package org.kodigo.proyectos.tasklist.dtos;

import lombok.Data;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.RepeatConfig;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class TaskDTO {

  private Long taskDTOId;
  private Long userId;

  private String name;

  private String description;

  private Long categoryId;

  private Relevance relevance;

  private LocalDate dueDate;

  private LocalTime specifiedTime;

  private RepeatConfig repeatConfig;
}
