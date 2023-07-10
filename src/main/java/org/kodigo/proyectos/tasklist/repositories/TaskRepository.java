package org.kodigo.proyectos.tasklist.repositories;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  Optional<Task> findByUserAndTaskId(UserEntity user, Long taskId);

  List<Task> findByUserAndCompletedDateIsNullOrderByDueDate(UserEntity user); // ALL PENDING

  List<Task> findByUserAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      UserEntity user); // ALL COMPLETED

  List<Task> findByUserAndDueDateAfterAndCompletedDateIsNullOrderByDueDate(
      UserEntity user, LocalDate dueDate); // FUTURE PENDING

  List<Task> findByUserAndDueDateBeforeAndCompletedDateIsNullOrderByDueDate(
      UserEntity user, LocalDate dueDate); // PREVIOUS PENDING

  List<Task> findByUserAndDueDateEqualsAndCompletedDateIsNullOrderByDueDate(
      UserEntity user, LocalDate dueDate); // PENDING FOR AN EXACT DATE

  List<Task> findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      UserEntity user, LocalDate completedDate); // PREVIOUS COMPLETED

  List<Task> findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullOrderByCompletedDateDesc(
      UserEntity user, LocalDate completedDate); // COMPLETED ON AN EXACT DATE

  List<Task> findByUserAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      UserEntity user, Relevance relevance); // ALL PENDING WITH RELEVANCE

  List<Task> findByUserAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
      UserEntity user, Relevance relevance); // ALL COMPLETED WITH RELEVANCE

  List<Task> findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      UserEntity user, LocalDate dueDate, Relevance relevance); // FUTURE PENDING WITH RELEVANCE

  List<Task> findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      UserEntity user, LocalDate dueDate, Relevance relevance); // PREVIOUS PENDING WITH RELEVANCE

  List<Task> findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
      UserEntity user,
      LocalDate dueDate,
      Relevance relevance); // PENDING FOR AN EXACT DATE WITH RELEVANCE

  List<Task>
      findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
          UserEntity user,
          LocalDate completedDate,
          Relevance relevance); // PREVIOUS COMPLETED WITH RELEVANCE

  List<Task>
      findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
          UserEntity user,
          LocalDate completedDate,
          Relevance relevance); // COMPLETED ON AN EXACT DATE WITH RELEVANCE

  List<Task> findByUserAndCompletedDateIsNullAndCategoryOrderByDueDate(
      UserEntity user, Category category); // ALL PENDING WITH CATEGORY

  List<Task> findByUserAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
      UserEntity user, Category category); // ALL COMPLETED WITH CATEGORY

  List<Task> findByUserAndDueDateAfterAndCompletedDateIsNullAndCategoryOrderByDueDate(
      UserEntity user, LocalDate dueDate, Category category); // FUTURE PENDING WITH CATEGORY

  List<Task> findByUserAndDueDateBeforeAndCompletedDateIsNullAndCategoryOrderByDueDate(
      UserEntity user, LocalDate dueDate, Category category); // PREVIOUS PENDING WITH CATEGORY

  List<Task> findByUserAndDueDateEqualsAndCompletedDateIsNullAndCategoryOrderByDueDate(
      UserEntity user,
      LocalDate dueDate,
      Category category); // PENDING FOR AN EXACT DATE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
          UserEntity user,
          LocalDate completedDate,
          Category category); // PREVIOUS COMPLETED WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
          UserEntity user,
          LocalDate completedDate,
          Category category); // COMPLETED ON AN EXACT DATE WITH CATEGORY

  List<Task> findByUserAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
      UserEntity user,
      Relevance relevance,
      Category category); // ALL PENDING WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
          UserEntity user,
          Relevance relevance,
          Category category); // ALL COMPLETED WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
          UserEntity user,
          LocalDate dueDate,
          Relevance relevance,
          Category category); // FUTURE PENDING WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
          UserEntity user,
          LocalDate dueDate,
          Relevance relevance,
          Category category); // PREVIOUS PENDING WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
          UserEntity user,
          LocalDate dueDate,
          Relevance relevance,
          Category category); // PENDING FOR AN EXACT DATE WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
          UserEntity user,
          LocalDate completedDate,
          Relevance relevance,
          Category category); // PREVIOUS COMPLETED WITH RELEVANCE WITH CATEGORY

  List<Task>
      findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
          UserEntity user,
          LocalDate completedDate,
          Relevance relevance,
          Category category); // COMPLETED ON AN EXACT DATE WITH RELEVANCE WITH CATEGORY
}
