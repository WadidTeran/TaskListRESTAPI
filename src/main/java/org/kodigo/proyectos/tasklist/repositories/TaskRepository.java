package org.kodigo.proyectos.tasklist.repositories;

import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  Optional<Task> findByUserAndName(User user, String name);

  List<Task> findByUserAndCompletedDateIsNullOrderByDueDate(User user); // ALL PENDING

  List<Task> findByUserAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      User user); // ALL COMPLETED

  List<Task> findByUserAndDueDateAfterAndCompletedDateIsNullOrderByDueDate(
      User user, LocalDate dueDate); // FUTURE PENDING

  List<Task> findByUserAndDueDateBeforeAndCompletedDateIsNullOrderByDueDate(
      User user, LocalDate dueDate); // PREVIOUS PENDING

  List<Task> findByUserAndDueDateEqualsAndCompletedDateIsNullOrderByDueDate(
      User user, LocalDate dueDate); // PENDING FOR AN EXACT DATE

  List<Task> findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      User user, LocalDate completedDate); // PREVIOUS COMPLETED

  List<Task> findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      User user, LocalDate completedDate); // COMPLETED ON AN EXACT DATE

  List<Task> findByUserAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      User user, Relevance relevance); // ALL PENDING WITH RELEVANCE

  List<Task> findByUserAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
      User user, Relevance relevance); // ALL COMPLETED WITH RELEVANCE

  List<Task> findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      User user, LocalDate dueDate, Relevance relevance); // FUTURE PENDING WITH RELEVANCE

  List<Task> findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      User user, LocalDate dueDate, Relevance relevance); // PREVIOUS PENDING WITH RELEVANCE

  List<Task> findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      User user,
      LocalDate dueDate,
      Relevance relevance); // PENDING FOR AN EXACT DATE WITH RELEVANCE

  List<Task>
      findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
          User user,
          LocalDate completedDate,
          Relevance relevance); // PREVIOUS COMPLETED WITH RELEVANCE

  List<Task>
      findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
          User user,
          LocalDate completedDate,
          Relevance relevance); // COMPLETED ON AN EXACT DATE WITH RELEVANCE
}
