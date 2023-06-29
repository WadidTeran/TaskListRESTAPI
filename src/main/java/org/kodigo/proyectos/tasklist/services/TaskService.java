package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
  @Autowired private TaskRepository taskRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private CategoryRepository categoryRepository;

  public boolean createTask(Task task) {

    try {
      task.setUser(userRepository.findById(task.getUser().getUserId()).get());
      if (task.getCategory() != null) {
        task.setCategory(categoryRepository.findById(task.getTaskId()).get());
      }
      if (task.getRepeatConfig() != null) {
        task.setRepeatConfig(taskRepository.findById(task.getTaskId()).get().getRepeatConfig());
      }
      taskRepository.save(task);
      return true;
    } catch (Exception ex) {
      System.err.println(ex);
      return false;
    }
  }

  public boolean modifyTask(Task task) {
    try {
      if (task.getCategory() != null) {
        task.setCategory(categoryRepository.findById(task.getCategory().getCategoryId()).get());
      }
      if (task.getRepeatConfig() != null) {
        task.setRepeatConfig(taskRepository.findById(task.getTaskId()).get().getRepeatConfig());
      }
      taskRepository.save(task);
      return true;
    } catch (Exception ex) {
      System.err.println(ex);
      return false;
    }
  }

  public boolean deleteTaskById(Long id) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    if (optionalTask.isPresent()) {
      taskRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }

  public Optional<Task> getTasksByName(User user, String nameTask) {
    return taskRepository.findByUserAndName(user, nameTask);
  }

  public List<Task> getAllPendingTasks(User user) {

    return taskRepository.findByUserAndCompletedDateIsNullOrderByDueDate(user);
  }

  public List<Task> getAllCompletedTasks(User user) {
    return taskRepository.findByUserAndCompletedDateIsNotNullOrderByCompletedDateDesc(user);
  }

  public List<Task> getFuturePendingTasksByDueDate(User user, LocalDate dueDate) {
    return taskRepository.findByUserAndDueDateAfterAndCompletedDateIsNullOrderByDueDate(
        user, dueDate);
  }

  public List<Task> getPreviousPendingTasksByDueDate(User user, LocalDate dueDate) {
    return taskRepository.findByUserAndDueDateBeforeAndCompletedDateIsNullOrderByDueDate(
        user, dueDate);
  }

  public List<Task> getPendingTaskByDate(User user, LocalDate dueDate) {
    return taskRepository.findByUserAndDueDateEqualsAndCompletedDateIsNullOrderByDueDate(
        user, dueDate);
  }

  public List<Task> getCompletedTasksByExactDate(User user, LocalDate date) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullOrderByCompletedDateDesc(
            user, date);
  }

  public List<Task> getPendingTasksByRelevance(User user, Relevance relevance) {
    return taskRepository.findByUserAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
        user, relevance);
  }

  public List<Task> getCompletedTasksByRelevance(User user, Relevance relevance) {
    return taskRepository
        .findByUserAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
            user, relevance);
  }

  public List<Task> getFuturePendingTasksByRelevance(
      User user, LocalDate date, Relevance relevance) {
    return taskRepository
        .findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
            user, date, relevance);
  }

  public List<Task> getPreviousPendingTasksByRelevance(
      User user, LocalDate date, Relevance relevance) {
    return taskRepository
        .findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
            user, date, relevance);
  }

  public List<Task> getPreviousCompletedTasks(User user, LocalDate date) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullOrderByCompletedDateDesc(
            user, date);
  }

  public List<Task> getPendingTasksForExactDateByRelevance(
      User user, LocalDate dueDate, Relevance relevance) {
    return taskRepository
        .findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
            user, dueDate, relevance);
  }

  public List<Task> getPreviousCompletedTasksByRelevance(
      User user, LocalDate completedDate, Relevance relevance) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
            user, completedDate, relevance);
  }

  public List<Task> getCompletedTasksOnExactDateByRelevance(
      User user, LocalDate completedDate, Relevance relevance) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
            user, completedDate, relevance);
  }

  public List<Task> getPendingTasksByCategory(User user, Category category) {
    return taskRepository.findByUserAndCompletedDateIsNullAndCategoryOrderByDueDate(user, category);
  }

  public List<Task> getCompletedTasksByCategory(User user, Category category) {
    return taskRepository.findByUserAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
        user, category);
  }

  public List<Task> getFuturePendingTasksByCategory(
      User user, LocalDate dueDate, Category category) {
    return taskRepository.findByUserAndDueDateAfterAndCompletedDateIsNullAndCategoryOrderByDueDate(
        user, dueDate, category);
  }

  public List<Task> getPreviousPendingTasksByCategory(
      User user, LocalDate dueDate, Category category) {
    return taskRepository.findByUserAndDueDateBeforeAndCompletedDateIsNullAndCategoryOrderByDueDate(
        user, dueDate, category);
  }

  public List<Task> getPendingTasksForExactDateByCategory(
      User user, LocalDate dueDate, Category category) {
    return taskRepository.findByUserAndDueDateEqualsAndCompletedDateIsNullAndCategoryOrderByDueDate(
        user, dueDate, category);
  }

  public List<Task> getPreviousCompletedTasksByCategory(
      User user, LocalDate completedDate, Category category) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
            user, completedDate, category);
  }

  public List<Task> getCompletedTasksOnExactDateByCategory(
      User user, LocalDate completedDate, Category category) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
            user, completedDate, category);
  }

  public List<Task> getPendingTasksByRelevanceAndCategory(
      User user, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
            user, relevance, category);
  }

  public List<Task> getCompletedTasksByRelevanceAndCategory(
      User user, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
            user, relevance, category);
  }

  public List<Task> getFuturePendingTasksByRelevanceAndCategory(
      User user, LocalDate dueDate, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
            user, dueDate, relevance, category);
  }

  public List<Task> getPreviousPendingTasksByRelevanceAndCategory(
      User user, LocalDate dueDate, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
            user, dueDate, relevance, category);
  }

  public List<Task> getPendingTasksForExactDateByRelevanceAndCategory(
      User user, LocalDate dueDate, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
            user, dueDate, relevance, category);
  }

  public List<Task> getPreviousCompletedTasksByRelevanceAndCategory(
      User user, LocalDate completedDate, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
            user, completedDate, relevance, category);
  }

  public List<Task> getCompletedTasksOnExactDateByRelevanceAndCategory(
      User user, LocalDate completedDate, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
            user, completedDate, relevance, category);
  }
}
