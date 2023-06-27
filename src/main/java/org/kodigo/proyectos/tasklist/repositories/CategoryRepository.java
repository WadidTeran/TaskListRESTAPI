package org.kodigo.proyectos.tasklist.repositories;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
}
