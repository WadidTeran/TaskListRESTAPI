package org.kodigo.proyectos.tasklist.repositories;

import org.kodigo.proyectos.tasklist.entities.Category;
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

  List<Task> findByUserAndCompletedDateIsNullAndCategoryOrderByDueDate(
      User user, Category category); // ALL PENDING WITH CATEGORY

  List<Task> findByUserAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
      User user, Category category); // ALL COMPLETED WITH CATEGORY

  List<Task> findByUserAndDueDateAfterAndCompletedDateIsNullAndCategoryOrderByDueDate(
      User user, LocalDate dueDate, Category category); // FUTURE PENDING WITH CATEGORY

  List<Task> findByUserAndDueDateBeforeAndCompletedDateIsNullAndCategoryOrderByDueDate(
      User user, LocalDate dueDate, Category category); // PREVIOUS PENDING WITH CATEGORY

  List<Task> findByUserAndDueDateEqualsAndCompletedDateIsNullAndCategoryOrderByDueDate(
      User user, LocalDate dueDate, Category category); // PENDING FOR AN EXACT DATE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
          User user,
          LocalDate completedDate,
          Category category); // PREVIOUS COMPLETED WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
          User user,
          LocalDate completedDate,
          Category category); // COMPLETED ON AN EXACT DATE WITH CATEGORY

  List<Task> findByUserAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
      User user,
      Relevance relevance,
      Category category); // ALL PENDING WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
          User user,
          Relevance relevance,
          Category category); // ALL COMPLETED WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
          User user,
          LocalDate dueDate,
          Relevance relevance,
          Category category); // FUTURE PENDING WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
          User user,
          LocalDate dueDate,
          Relevance relevance,
          Category category); // PREVIOUS PENDING WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
          User user,
          LocalDate dueDate,
          Relevance relevance,
          Category category); // PENDING FOR AN EXACT DATE WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
          User user,
          LocalDate completedDate,
          Relevance relevance,
          Category category); // PREVIOUS COMPLETED WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
          User user,
          LocalDate completedDate,
          Relevance relevance,
          Category category); // COMPLETED ON AN EXACT DATE WITH RELEVANCE WITH CATEGORY
}
