package org.kodigo.proyectos.tasklist.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.TaskRepository;
import org.kodigo.proyectos.tasklist.services.TaskService;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.kodigo.proyectos.tasklist.testutils.TestHelper;
import org.kodigo.proyectos.tasklist.testutils.TestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {
  private final String BASE_URL = "/tasks";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private TestRestTemplate testRestTemplate;
  @Autowired private RestTemplateBuilder restTemplateBuilder;
  @Autowired private TaskService taskService;
  @Autowired private UserService userService;
  @Autowired private TaskRepository taskRepository;
  @LocalServerPort private int port;
  @Autowired private TestHelper testHelper;

  @BeforeEach
  void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
    testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    objectMapper.registerModule(new JavaTimeModule());
  }

  @DisplayName("GET /tasks")
  @Test
  void getTaskList() {}

  @DisplayName("POST /tasks")
  @Test
  void createTask() throws JsonProcessingException {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();
    Task createdTask =
        new Task(userEntity, "New task", null, null, Relevance.NONE, LocalDate.now(), null, null);
    String body = objectMapper.writeValueAsString(createdTask);

    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(body, headers);

    ResponseEntity<Task> response =
        testRestTemplate.exchange(BASE_URL, HttpMethod.POST, request, Task.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    UserEntity userEntityN = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();
    Task createdTaskDB =
        taskService
            .findByUserAndTaskId(
                userEntity,
                userEntityN.getTasks().get(userEntityN.getTasks().size() - 1).getTaskId())
            .orElseThrow();

    assertEquals("New task", createdTaskDB.getName());

    testHelper.deleteTestUser();
  }

  @DisplayName("PUT /tasks")
  @Test
  void modifyTask() throws JsonProcessingException {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();

    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    Task modifiedTask =
        taskRepository
            .findByUserAndTaskId(userEntity, userEntity.getTasks().get(0).getTaskId())
            .orElseThrow();

    modifiedTask.setName("ModifiedTask");
    String body = objectMapper.writeValueAsString(modifiedTask);
    HttpEntity<String> request = new HttpEntity<>(body, headers);

    ResponseEntity<Task> response =
        testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, request, Task.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(modifiedTask.getName(), response.getBody().getName());

    testHelper.deleteTestUser();
  }

  @DisplayName("DELETE /tasks")
  @Test
  void deleteTasks() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);
    ResponseEntity<?> responseNoContent =
        testRestTemplate.exchange(
            BASE_URL, HttpMethod.DELETE, request, new ParameterizedTypeReference<>() {});
    ResponseEntity<?> responseNotFound =
        testRestTemplate.exchange(
            BASE_URL, HttpMethod.DELETE, request, new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responseNoContent.getStatusCode());
    assertEquals(HttpStatus.NOT_FOUND, responseNotFound.getStatusCode());
    testHelper.deleteTestUser();
  }

  @DisplayName("GET /tasks/{taskId}")
  @Test
  void getTaskById() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();
    Long id = userEntity.getTasks().get(0).getTaskId();
    ResponseEntity<Task> response =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", id)), HttpMethod.GET, request, Task.class);
    ResponseEntity<Task> response2 =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", -1L)), HttpMethod.GET, request, Task.class);

    Task responseTask = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    assertEquals(responseTask.getTaskId(), id);

    testHelper.deleteTestUser();
  }

  @DisplayName("PUT /tasks/{taskId}")
  @Test
  void changeTaskStatus() {}

  @DisplayName("DELETE /tasks/{taskId}")
  @Test
  void deleteTask() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    UserEntity userEntity = userService.getUserByUsername(TestUser.USERNAME.value).orElseThrow();

    Long id = userEntity.getTasks().get(0).getTaskId();

    ResponseEntity<Task> response =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", id)), HttpMethod.DELETE, request, Task.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    Optional<Task> deletedTask = taskService.findByUserAndTaskId(userEntity, id);

    assertTrue(deletedTask.isEmpty());
    testHelper.deleteTestUser();
  }
}
