package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.dtos.TaskDTO;
import org.kodigo.proyectos.tasklist.entities.*;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
  @Autowired private TaskRepository taskRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private CategoryService categoryService;
  @Autowired private UserService userService;
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

  public Optional<List<Task>> getTaskList(
      User user, String status, String due, String rel, String category) {
    if (!validateParams(user, status, due, rel, category)) return Optional.empty();

    if (userService.validateUser(user)) {
      User userDB =
          (user.getEmail() != null)
              ? userService.getUserByEmail(user.getEmail()).orElseThrow()
              : userService.getUserByUsername(user.getUsername()).orElseThrow();

      String[] params =
          new String[] {
            status,
            due,
            (rel == null) ? null : ParamStrings.RELEVANCE.value,
            (category == null) ? null : ParamStrings.CATEGORY.value
          };
      for (TaskQuery query : TaskQuery.values()) {
        if (Arrays.equals(params, query.params)) {
          return executeTaskQuery(query, userDB, rel, category);
        }
      }
    }

    return Optional.empty();
  }

  private Optional<List<Task>> executeTaskQuery(
      TaskQuery query, User user, String rel, String categoryName) {
    Relevance relevance = (rel != null) ? Relevance.fromValue(rel) : null;
    Category category =
        (categoryName != null)
            ? categoryService.getCategoryByName(user, categoryName).orElseThrow()
            : null;

    switch (query) {
      case ALL_COMPLETED -> {
        return Optional.of(
            taskRepository.findByUserAndCompletedDateIsNotNullOrderByCompletedDateDesc(user));
      }
      case ALL_PENDING -> {
        return Optional.of(taskRepository.findByUserAndCompletedDateIsNullOrderByDueDate(user));
      }
      case FUTURE_PENDING -> {
        return Optional.of(
            taskRepository.findByUserAndDueDateAfterAndCompletedDateIsNullOrderByDueDate(
                user, LocalDate.now()));
      }
      case PREVIOUS_PENDING -> {
        return Optional.of(
            taskRepository.findByUserAndDueDateBeforeAndCompletedDateIsNullOrderByDueDate(
                user, LocalDate.now()));
      }
      case PENDING_FOR_TODAY -> {
        return Optional.of(
            taskRepository.findByUserAndDueDateEqualsAndCompletedDateIsNullOrderByDueDate(
                user, LocalDate.now()));
      }
      case COMPLETED_TODAY -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullOrderByCompletedDateDesc(
                    user, LocalDate.now()));
      }
      case PREVIOUS_COMPLETED -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullOrderByCompletedDateDesc(
                    user, LocalDate.now()));
      }
      case PENDING_BY_RELEVANCE -> {
        return Optional.of(
            taskRepository.findByUserAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
                user, relevance));
      }
      case COMPLETED_BY_RELEVANCE -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
                    user, relevance));
      }
      case FUTURE_PENDING_BY_RELEVANCE -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
                    user, LocalDate.now(), relevance));
      }
      case PREVIOUS_PENDING_BY_RELEVANCE -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
                    user, LocalDate.now(), relevance));
      }
      case PENDING_FOR_TODAY_BY_RELEVANCE -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsOrderByDueDate(
                    user, LocalDate.now(), relevance));
      }
      case PREVIOUS_COMPLETED_BY_RELEVANCE -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
                    user, LocalDate.now(), relevance));
      }
      case COMPLETED_TODAY_BY_RELEVANCE -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsOrderByCompletedDateDesc(
                    user, LocalDate.now(), relevance));
      }
      case PENDING_BY_CATEGORY -> {
        return Optional.of(
            taskRepository.findByUserAndCompletedDateIsNullAndCategoryOrderByDueDate(
                user, category));
      }
      case COMPLETED_BY_CATEGORY -> {
        return Optional.of(
            taskRepository.findByUserAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
                user, category));
      }
      case FUTURE_PENDING_BY_CATEGORY -> {
        return Optional.of(
            taskRepository.findByUserAndDueDateAfterAndCompletedDateIsNullAndCategoryOrderByDueDate(
                user, LocalDate.now(), category));
      }
      case PREVIOUS_PENDING_BY_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateBeforeAndCompletedDateIsNullAndCategoryOrderByDueDate(
                    user, LocalDate.now(), category));
      }
      case PENDING_FOR_TODAY_BY_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateEqualsAndCompletedDateIsNullAndCategoryOrderByDueDate(
                    user, LocalDate.now(), category));
      }
      case PREVIOUS_COMPLETED_BY_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
                    user, LocalDate.now(), category));
      }
      case COMPLETED_TODAY_BY_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndCategoryOrderByCompletedDateDesc(
                    user, LocalDate.now(), category));
      }
      case PENDING_BY_RELEVANCE_AND_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
                    user, relevance, category));
      }
      case COMPLETED_BY_RELEVANCE_AND_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
                    user, relevance, category));
      }
      case FUTURE_PENDING_BY_RELEVANCE_AND_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateAfterAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
                    user, LocalDate.now(), relevance, category));
      }
      case PREVIOUS_PENDING_BY_RELEVANCE_AND_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateBeforeAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
                    user, LocalDate.now(), relevance, category));
      }
      case PENDING_FOR_TODAY_BY_RELEVANCE_AND_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndDueDateEqualsAndCompletedDateIsNullAndRelevanceEqualsAndCategoryOrderByDueDate(
                    user, LocalDate.now(), relevance, category));
      }
      case PREVIOUS_COMPLETED_BY_RELEVANCE_AND_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateBeforeAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
                    user, LocalDate.now(), relevance, category));
      }
      case COMPLETED_TODAY_BY_RELEVANCE_AND_CATEGORY -> {
        return Optional.of(
            taskRepository
                .findByUserAndCompletedDateEqualsAndCompletedDateIsNotNullAndRelevanceEqualsAndCategoryOrderByCompletedDateDesc(
                    user, LocalDate.now(), relevance, category));
      }
      default -> {
        return Optional.empty();
      }
    }
  }

  private boolean validateParams(
      User user, String status, String due, String rel, String category) {
    return !((status == null
            || (!status.equals(ParamStrings.COMPLETED.value)
                && !status.equals(ParamStrings.PENDING.value)))
        || (due != null
            && !due.equals(ParamStrings.TODAY.value)
            && !due.equals(ParamStrings.PREVIOUS.value)
            && !due.equals(ParamStrings.FUTURE.value))
        || (rel != null
            && !rel.equals(Relevance.NONE.value)
            && !rel.equals(Relevance.LOW.value)
            && !rel.equals(Relevance.MEDIUM.value)
            && !rel.equals(Relevance.HIGH.value))
        || (category != null && !categoryService.existCategory(user, category))
        || (status.equals(ParamStrings.COMPLETED.toString()))
            && due != null
            && due.equals(ParamStrings.FUTURE.value));
  }
}
