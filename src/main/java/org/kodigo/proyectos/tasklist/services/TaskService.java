package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.dtos.TaskDTO;
import org.kodigo.proyectos.tasklist.entities.*;
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

  public boolean createTask(TaskDTO taskDTO) {
    if (validateTaskDTO(taskDTO)) {
      Task task = convertToTask(taskDTO);
      taskRepository.save(task);
      return true;
    }
    return false;
  }

  public boolean modifyTask(TaskDTO taskDTO) {
    if (validateTaskDTO(taskDTO)) {
      Task task = convertToTask(taskDTO);
      task.setTaskId(taskDTO.getTaskDTOId());
      taskRepository.save(task);
      return true;
    }
    return false;
  }

  public boolean deleteTask(Task task) {
    if (taskRepository.existsById(task.getTaskId())) {
      taskRepository.deleteById(task.getTaskId());
      return true;
    }
    return false;
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

  public List<Task> getFuturePendingTasks(User user) {
    return taskRepository.findByUserAndDueDateAfterAndCompletedDateIsNullOrderByDueDate(
        user, LocalDate.now());
  }

  public List<Task> getPreviousPendingTasks(User user) {
    return taskRepository.findByUserAndDueDateBeforeAndCompletedDateIsNullOrderByDueDate(
        user, LocalDate.now());
  }

  public List<Task> getPendingTaskForToday(User user) {
    return taskRepository.findByUserAndDueDateEqualsAndCompletedDateIsNullOrderByDueDate(
        user, LocalDate.now());
  }

  public List<Task> getCompletedTasksToday(User user) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullOrderByCompletedDateDesc(
            user, LocalDate.now());
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

  public List<Task> getFuturePendingTasksByRelevance(User user, Relevance relevance) {
    return taskRepository
        .findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
            user, LocalDate.now(), relevance);
  }

  public List<Task> getPreviousPendingTasksByRelevance(User user, Relevance relevance) {
    return taskRepository
        .findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
            user, LocalDate.now(), relevance);
  }

  public List<Task> getPreviousCompletedTasks(User user) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullOrderByCompletedDateDesc(
            user, LocalDate.now());
  }

  public List<Task> getPendingTasksForTodayByRelevance(User user, Relevance relevance) {
    return taskRepository
        .findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
            user, LocalDate.now(), relevance);
  }

  public List<Task> getPreviousCompletedTasksByRelevance(User user, Relevance relevance) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
            user, LocalDate.now(), relevance);
  }

  public List<Task> getCompletedTasksTodayByRelevance(User user, Relevance relevance) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
            user, LocalDate.now(), relevance);
  }

  public List<Task> getPendingTasksByCategory(User user, Category category) {
    return taskRepository.findByUserAndCompletedDateIsNullAndCategoryOrderByDueDate(user, category);
  }

  public List<Task> getCompletedTasksByCategory(User user, Category category) {
    return taskRepository.findByUserAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
        user, category);
  }

  public List<Task> getFuturePendingTasksByCategory(User user, Category category) {
    return taskRepository.findByUserAndDueDateAfterAndCompletedDateIsNullAndCategoryOrderByDueDate(
        user, LocalDate.now(), category);
  }

  public List<Task> getPreviousPendingTasksByCategory(User user, Category category) {
    return taskRepository.findByUserAndDueDateBeforeAndCompletedDateIsNullAndCategoryOrderByDueDate(
        user, LocalDate.now(), category);
  }

  public List<Task> getPendingTasksForTodayByCategory(User user, Category category) {
    return taskRepository.findByUserAndDueDateEqualsAndCompletedDateIsNullAndCategoryOrderByDueDate(
        user, LocalDate.now(), category);
  }

  public List<Task> getPreviousCompletedTasksByCategory(User user, Category category) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
            user, LocalDate.now(), category);
  }

  public List<Task> getCompletedTasksTodayByCategory(User user, Category category) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
            user, LocalDate.now(), category);
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
      User user, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
            user, LocalDate.now(), relevance, category);
  }

  public List<Task> getPreviousPendingTasksByRelevanceAndCategory(
      User user, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
            user, LocalDate.now(), relevance, category);
  }

  public List<Task> getPendingTasksForTodayByRelevanceAndCategory(
      User user, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
            user, LocalDate.now(), relevance, category);
  }

  public List<Task> getPreviousCompletedTasksByRelevanceAndCategory(
      User user, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
            user, LocalDate.now(), relevance, category);
  }

  public List<Task> getCompletedTasksTodayByRelevanceAndCategory(
      User user, Relevance relevance, Category category) {
    return taskRepository
        .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
            user, LocalDate.now(), relevance, category);
  }

  private Task convertToTask(TaskDTO taskDTO) {

    Task task = new Task();
    task.setName(taskDTO.getName());
    task.setUser(userRepository.findById(taskDTO.getUserId()).orElseThrow());

    if (taskDTO.getCategoryId() != null) {
      task.setCategory(categoryRepository.findById(taskDTO.getCategoryId()).orElseThrow());
    }
    if (taskDTO.getDescription() != null) {
      task.setDescription(taskDTO.getDescription());
    }
    if (taskDTO.getDueDate() != null) {
      task.setDueDate(taskDTO.getDueDate());
    }
    if (taskDTO.getSpecifiedTime() != null) {
      task.setSpecifiedTime(taskDTO.getSpecifiedTime());
    }
    if (taskDTO.getRepeatConfig() != null) {
      task.setRepeatConfig(taskDTO.getRepeatConfig());
    }
    if (taskDTO.getRelevance() != null) {
      task.setRelevance(taskDTO.getRelevance());
    }

    return task;
  }

  private boolean validateTaskDTO(TaskDTO taskDTO) {
    String dtoName = taskDTO.getName();
    Long dtoUserId = taskDTO.getUserId();
    Long dtoCategoryId = taskDTO.getCategoryId();
    RepeatConfig dtoRepeatConfig = taskDTO.getRepeatConfig();
    return !(dtoUserId == null
        || !userRepository.existsById(dtoUserId)
        || dtoName == null
        || dtoName.isEmpty()
        || dtoName.isBlank()
        || (dtoCategoryId != null && !categoryRepository.existsById(dtoCategoryId))
        || (dtoRepeatConfig != null
            && (taskDTO.getDueDate() == null
                || dtoRepeatConfig.getRepeatInterval() == null
                || dtoRepeatConfig.getRepeatType() == null
                || (dtoRepeatConfig.getRepeatType() == RepeatType.HOUR
                    && taskDTO.getSpecifiedTime() == null))));
  }
}
