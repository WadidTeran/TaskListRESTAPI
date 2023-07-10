package org.kodigo.proyectos.tasklist.testutils;

import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TestHelper {
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private TaskRepository taskRepository;
  @Autowired private UserService userService;

  public void registerTestUser(TestRestTemplate testRestTemplate) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    String body =
        String.format(
            "{\"email\": \"%s\", \"username\": \"%s\", \"password\": \"%s\"}",
            TestUser.EMAIL.value, TestUser.USERNAME.value, TestUser.PASSWORD.value);

    HttpEntity<String> request = new HttpEntity<>(body, headers);
    testRestTemplate.exchange("/register", HttpMethod.POST, request, UserEntity.class);
  }

  public void createTestData() {
    UserEntity user = userService.getUserByUsername(TestUser.USERNAME.value).orElseThrow();

    Category category1 = new Category("Test category 1", user);
    Category category2 = new Category("Test category 2", user);

    category1 = categoryRepository.save(category1);
    category2 = categoryRepository.save(category2);

    Task task1 =
        new Task(
            user,
            "task1",
            "Future pending with relevance none and category 1",
            category1,
            Relevance.NONE,
            LocalDate.now().plusMonths(1),
            null,
            null);
    Task task2 =
        new Task(
            user,
            "task2",
            "Future pending with relevance low and category 2",
            category2,
            Relevance.LOW,
            LocalDate.now().plusMonths(2),
            null,
            null);
    Task task3 =
        new Task(
            user,
            "task3",
            "Future pending with relevance medium and category 1",
            category1,
            Relevance.MEDIUM,
            LocalDate.now().plusMonths(3),
            null,
            null);
    Task task4 =
        new Task(
            user,
            "task4",
            "Future pending with relevance high and category 2",
            category2,
            Relevance.HIGH,
            LocalDate.now().plusMonths(4),
            null,
            null);
    Task task5 =
        new Task(
            user,
            "task5",
            "Previous pending with relevance none and category 1",
            category1,
            Relevance.NONE,
            LocalDate.now().minusMonths(4),
            null,
            null);
    Task task6 =
        new Task(
            user,
            "task6",
            "Previous pending with relevance low and category 2",
            category2,
            Relevance.LOW,
            LocalDate.now().minusMonths(3),
            null,
            null);
    Task task7 =
        new Task(
            user,
            "task7",
            "Previous pending with relevance medium and category 1",
            category1,
            Relevance.MEDIUM,
            LocalDate.now().minusMonths(2),
            null,
            null);
    Task task8 =
        new Task(
            user,
            "task8",
            "Previous pending with relevance high and category 2",
            category2,
            Relevance.HIGH,
            LocalDate.now().minusMonths(1),
            null,
            null);
    Task task9 =
        new Task(
            user,
            "task9",
            "Pending for today with relevance none and category 1",
            category1,
            Relevance.NONE,
            LocalDate.now(),
            null,
            null);
    Task task10 =
        new Task(
            user,
            "task10",
            "Pending for today with relevance low and category 2",
            category2,
            Relevance.LOW,
            LocalDate.now(),
            null,
            null);
    Task task11 =
        new Task(
            user,
            "task11",
            "Pending for today with relevance medium and category 1",
            category1,
            Relevance.MEDIUM,
            LocalDate.now(),
            null,
            null);
    Task task12 =
        new Task(
            user,
            "task12",
            "Pending for today with relevance high and category 2",
            category2,
            Relevance.HIGH,
            LocalDate.now(),
            null,
            null);

    Task task13 =
        new Task(
            user,
            "task13",
            "Previous completed with relevance none and category 1",
            category1,
            Relevance.NONE,
            LocalDate.now(),
            null,
            null);
    task13.setCompletedDate(LocalDate.now().minusMonths(4));
    Task task14 =
        new Task(
            user,
            "task14",
            "Previous completed with relevance low and category 2",
            category2,
            Relevance.LOW,
            LocalDate.now(),
            null,
            null);
    task14.setCompletedDate(LocalDate.now().minusMonths(3));
    Task task15 =
        new Task(
            user,
            "task15",
            "Previous completed with relevance medium and category 1",
            category1,
            Relevance.MEDIUM,
            LocalDate.now(),
            null,
            null);
    task15.setCompletedDate(LocalDate.now().minusMonths(2));
    Task task16 =
        new Task(
            user,
            "task16",
            "Previous completed with relevance high and category 2",
            category2,
            Relevance.HIGH,
            LocalDate.now(),
            null,
            null);
    task16.setCompletedDate(LocalDate.now().minusMonths(1));
    Task task17 =
        new Task(
            user,
            "task17",
            "Completed today with relevance none and category 1",
            category1,
            Relevance.NONE,
            LocalDate.now(),
            null,
            null);
    task17.setCompletedDate(LocalDate.now());
    Task task18 =
        new Task(
            user,
            "task18",
            "Completed today with relevance low and category 2",
            category2,
            Relevance.LOW,
            LocalDate.now(),
            null,
            null);
    task18.setCompletedDate(LocalDate.now());
    Task task19 =
        new Task(
            user,
            "task19",
            "Completed today with relevance medium and category 1",
            category1,
            Relevance.MEDIUM,
            LocalDate.now(),
            null,
            null);
    task19.setCompletedDate(LocalDate.now());
    Task task20 =
        new Task(
            user,
            "task20",
            "Completed today with relevance high and category 2",
            category2,
            Relevance.HIGH,
            LocalDate.now(),
            null,
            null);
    task20.setCompletedDate(LocalDate.now());

    taskRepository.save(task1);
    taskRepository.save(task2);
    taskRepository.save(task3);
    taskRepository.save(task4);
    taskRepository.save(task5);
    taskRepository.save(task6);
    taskRepository.save(task7);
    taskRepository.save(task8);
    taskRepository.save(task9);
    taskRepository.save(task10);
    taskRepository.save(task11);
    taskRepository.save(task12);
    taskRepository.save(task13);
    taskRepository.save(task14);
    taskRepository.save(task15);
    taskRepository.save(task16);
    taskRepository.save(task17);
    taskRepository.save(task18);
    taskRepository.save(task19);
    taskRepository.save(task20);
  }

  public String getJWTForTestUser(TestRestTemplate testRestTemplate) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    String body =
        String.format(
            "{\"username\": \"%s\", \"password\": \"%s\"}",
            TestUser.USERNAME.value, TestUser.PASSWORD.value);

    HttpEntity<String> loginRequest = new HttpEntity<>(body, headers);

    ResponseEntity<?> response =
        testRestTemplate.exchange(
            "/login", HttpMethod.POST, loginRequest, new ParameterizedTypeReference<>() {});

    return response.getHeaders().getFirst("Authorization");
  }

  public void deleteTestUser() {
    userService.getUserByUsername(TestUser.USERNAME.value).ifPresent(userService::deleteUser);
  }

  public HttpHeaders getHeadersWithAuthenticationForTestUser(TestRestTemplate testRestTemplate) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.set("Authorization", "Bearer ".concat(getJWTForTestUser(testRestTemplate)));
    return headers;
  }
}
