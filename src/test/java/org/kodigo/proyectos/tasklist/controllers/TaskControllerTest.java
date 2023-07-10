package org.kodigo.proyectos.tasklist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.CategoryService;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {
  private final String BASE_URL = "/tasks";
  private TestRestTemplate testRestTemplate;
  @Autowired private RestTemplateBuilder restTemplateBuilder;
  @Autowired private TaskService taskService;
  @LocalServerPort private int port;
  @Autowired private TestHelper testHelper;
  @Autowired private UserService userService;
  @Autowired private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
    testRestTemplate = new TestRestTemplate(restTemplateBuilder);
  }

  @DisplayName("GET /tasks")
  @Test
  void getTaskList() {}

  @DisplayName("POST /tasks")
  @Test
  void createTask() {}

  @DisplayName("PUT /tasks")
  @Test
  void modifyTask() {}

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
  void deleteTask() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    UserEntity userEntity = userService.getUserByUsername(TestUser.USERNAME.value).orElseThrow();

    Long id = 1L;

    Task task = taskService.findByUserAndTaskId(userEntity, id).orElseThrow();

    ResponseEntity<Task> response =
            testRestTemplate.exchange(
                    BASE_URL.concat(String.format("/%d", id)), HttpMethod.DELETE, request, Task.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    Optional<Task> deletedTask =
            taskService.findByUserAndTaskId(userEntity,id);

    assertTrue(deletedTask.isEmpty());
    testHelper.deleteTestUser();
  }
}
