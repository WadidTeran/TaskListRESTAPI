package org.kodigo.proyectos.tasklist.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "repeat_configs")
@Data
@NoArgsConstructor
public class RepeatConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "repeat_config_id")
  private Long repeatConfigId;

  @Column(name = "repeat_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private RepeatType repeatType;

  @Column(name = "repeat_interval", nullable = false)
  private Integer repeatInterval;

  @Column(name = "repeat_ends_at")
  private LocalDate repeatEndsAt;

  public RepeatConfig(RepeatType repeatType, Integer repeatInterval, LocalDate repeatEndsAt) {
    this.repeatType = repeatType;
    this.repeatInterval = repeatInterval;
    this.repeatEndsAt = repeatEndsAt;
  }
}
