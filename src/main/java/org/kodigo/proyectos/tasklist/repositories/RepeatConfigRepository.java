package org.kodigo.proyectos.tasklist.repositories;

import org.kodigo.proyectos.tasklist.entities.RepeatConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatConfigRepository extends JpaRepository<RepeatConfig, Long> {}
