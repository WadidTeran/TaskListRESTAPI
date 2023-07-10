package org.kodigo.proyectos.tasklist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.services.TaskService;
import org.kodigo.proyectos.tasklist.testutils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {
  private final String BASE_URL = "/tasks";
  private TestRestTemplate testRestTemplate;
  @Autowired private RestTemplateBuilder restTemplateBuilder;
  @Autowired private TaskService taskService;
  @LocalServerPort private int port;
  @Autowired private TestHelper testHelper;

  @BeforeEach
  void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
    testRestTemplate = new TestRestTemplate(restTemplateBuilder);
  }

  @Test
  void getTaskList() {}

  @Test
  void createTask() {}

  @Test
  void modifyTask() {}

  @Test
  void deleteTasks() {}

  @Test
  void getTaskById() {}

  @Test
  void changeTaskStatus() {}

  @Test
  void deleteTask() {}
}
