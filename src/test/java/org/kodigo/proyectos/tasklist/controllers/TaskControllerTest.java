package org.kodigo.proyectos.tasklist.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.http.*;

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
  void createTask() {}

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
  void deleteTasks() {}

  @DisplayName("GET /tasks/{taskId}")
  @Test
  void getTaskById() {}

  @DisplayName("PUT /tasks/{taskId}")
  @Test
  void changeTaskStatus() {}

  @DisplayName("DELETE /tasks/{taskId}")
  @Test
  void deleteTask() {}
}
