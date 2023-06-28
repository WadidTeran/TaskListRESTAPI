package org.kodigo.proyectos.tasklist.repositories;

import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  Optional<Task> findByName(String name);

  List<Task> findByCompletedDateIsNullOrderByDueDateDesc(); // ALL PENDING

  List<Task> findByCompletedDateIsNotNullOrderByCompletedDateDesc(); // ALL COMPLETED

  List<Task> findByDueDateAfterAndCompletedDateIsNullOrderByDueDateDesc(
      LocalDate dueDate); // FUTURE PENDING

  List<Task> findByDueDateBeforeAndCompletedDateIsNullOrderByDueDateDesc(
      LocalDate dueDate); // PREVIOUS PENDING

  List<Task> findByDueDateEqualsAndCompletedDateIsNullOrderByDueDateDesc(
      LocalDate dueDate); // PENDING FOR AN EXACT DATE

  List<Task> findByCompletedDateAfterAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      LocalDate dueDate); // FUTURE COMPLETED

  List<Task> findByCompletedDateBeforeAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      LocalDate dueDate); // PREVIOUS COMPLETED

  List<Task> findByCompletedDateEqualsAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      LocalDate dueDate); // COMPLETED ON AN EXACT DATE

  List<Task> findByCompletedDateIsNullAndRelevanceEqualsOrderByDueDateDesc(
      Relevance relevance); // ALL PENDING WITH RELEVANCE

  List<Task> findByCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
      Relevance relevance); // ALL COMPLETED WITH RELEVANCE

  List<Task> findByDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDateDesc(
      LocalDate dueDate, Relevance relevance); // FUTURE PENDING WITH RELEVANCE

  List<Task> findByDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDateDesc(
      LocalDate dueDate, Relevance relevance); // PREVIOUS PENDING WITH RELEVANCE

  List<Task> findByDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDateDesc(
      LocalDate dueDate, Relevance relevance); // PENDING FOR AN EXACT DATE WITH RELEVANCE

  List<Task>
      findByCompletedDateAfterAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
          LocalDate dueDate, Relevance relevance); // FUTURE COMPLETED WITH RELEVANCE

  List<Task>
      findByCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
          LocalDate dueDate, Relevance relevance); // PREVIOUS COMPLETED WITH RELEVANCE

  List<Task>
      findByCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
          LocalDate dueDate, Relevance relevance); // COMPLETED ON AN EXACT DATE WITH RELEVANCE
}
