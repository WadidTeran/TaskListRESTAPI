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
import org.kodigo.proyectos.tasklist.repositories.CategoryRepository;
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
import java.util.List;
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
  @Autowired private CategoryRepository categoryRepository;
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
  void getTaskList() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();

    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    ResponseEntity<List<Task>> responseAllCompleted =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseAllCompleted.getStatusCode());
    assertEquals(8, responseAllCompleted.getBody().size());

    ResponseEntity<List<Task>> responseAllPending =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseAllPending.getStatusCode());
    assertEquals(12, responseAllPending.getBody().size());

    ResponseEntity<List<Task>> responseFuturePending =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=future",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePending.getStatusCode());
    assertEquals(4, responseFuturePending.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPending =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=previous",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPending.getStatusCode());
    assertEquals(4, responsePreviousPending.getBody().size());

    ResponseEntity<List<Task>> responsePendingForToday =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=today",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForToday.getStatusCode());
    assertEquals(4, responsePendingForToday.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompleted =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=previous",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompleted.getStatusCode());
    assertEquals(4, responsePreviousCompleted.getBody().size());

    ResponseEntity<List<Task>> responseCompletedToday =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=today",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedToday.getStatusCode());
    assertEquals(4, responseCompletedToday.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceNone =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&rel=none",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceNone.getStatusCode());
    assertEquals(3, responsePendingRelevanceNone.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceLow =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&rel=low",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceLow.getStatusCode());
    assertEquals(3, responsePendingRelevanceLow.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceMedium =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&rel=medium",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceMedium.getStatusCode());
    assertEquals(3, responsePendingRelevanceMedium.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceHigh =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&rel=high",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceHigh.getStatusCode());
    assertEquals(3, responsePendingRelevanceHigh.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceNone =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&rel=none",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceNone.getStatusCode());
    assertEquals(2, responseCompletedRelevanceNone.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceLow =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&rel=low",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceLow.getStatusCode());
    assertEquals(2, responseCompletedRelevanceLow.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceMedium =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&rel=medium",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceMedium.getStatusCode());
    assertEquals(2, responseCompletedRelevanceMedium.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceHigh =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&rel=high",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceHigh.getStatusCode());
    assertEquals(2, responseCompletedRelevanceHigh.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceNone =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=future&rel=none",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceNone.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceNone.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceLow =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=future&rel=low",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceLow.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceLow.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceMedium =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=future&rel=medium",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceMedium.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceMedium.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceHigh =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=future&rel=high",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceHigh.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceHigh.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceNone =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=previous&rel=none",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceNone.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceNone.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceLow =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=previous&rel=low",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceLow.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceLow.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceMedium =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=previous&rel=medium",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceMedium.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceMedium.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceHigh =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=previous&rel=high",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceHigh.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceHigh.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceNone =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=today&rel=none",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceNone.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceNone.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceLow =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=today&rel=low",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceLow.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceLow.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceMedium =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=today&rel=medium",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceMedium.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceMedium.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceHigh =
        testRestTemplate.exchange(
            BASE_URL + "?status=pending&due=today&rel=high",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceHigh.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceHigh.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceNone =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=previous&rel=none",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceNone.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceNone.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceLow =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=previous&rel=low",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceLow.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceLow.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceMedium =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=previous&rel=medium",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceMedium.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceMedium.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceHigh =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=previous&rel=high",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceHigh.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceHigh.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceNone =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=today&rel=none",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceNone.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceNone.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceLow =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=today&rel=low",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceLow.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceLow.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceMedium =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=today&rel=medium",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceMedium.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceMedium.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceHigh =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=today&rel=high",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceHigh.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceHigh.getBody().size());

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();
    Long categoryId1 =
        categoryRepository
            .findByUserAndName(userEntity, "Test category 1")
            .orElseThrow()
            .getCategoryId();
    Long categoryId2 =
        categoryRepository
            .findByUserAndName(userEntity, "Test category 2")
            .orElseThrow()
            .getCategoryId();

    ResponseEntity<List<Task>> responsePendingCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingCategory1.getStatusCode());
    assertEquals(6, responsePendingCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePendingCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingCategory2.getStatusCode());
    assertEquals(6, responsePendingCategory2.getBody().size());

    ResponseEntity<List<Task>> responseCompletedCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedCategory1.getStatusCode());
    assertEquals(4, responseCompletedCategory1.getBody().size());

    ResponseEntity<List<Task>> responseCompletedCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedCategory2.getStatusCode());
    assertEquals(4, responseCompletedCategory2.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&due=future&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingCategory1.getStatusCode());
    assertEquals(2, responseFuturePendingCategory1.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&due=future&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingCategory2.getStatusCode());
    assertEquals(2, responseFuturePendingCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&due=previous&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingCategory1.getStatusCode());
    assertEquals(2, responsePreviousPendingCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&due=previous&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingCategory2.getStatusCode());
    assertEquals(2, responsePreviousPendingCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&due=today&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayCategory1.getStatusCode());
    assertEquals(2, responsePendingForTodayCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&due=today&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayCategory2.getStatusCode());
    assertEquals(2, responsePendingForTodayCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&due=previous&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedCategory1.getStatusCode());
    assertEquals(2, responsePreviousCompletedCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&due=previous&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedCategory2.getStatusCode());
    assertEquals(2, responsePreviousCompletedCategory2.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&due=today&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayCategory1.getStatusCode());
    assertEquals(2, responseCompletedTodayCategory1.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&due=today&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayCategory2.getStatusCode());
    assertEquals(2, responseCompletedTodayCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceNoneCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=none&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceNoneCategory1.getStatusCode());
    assertEquals(3, responsePendingRelevanceNoneCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceLowCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=low&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responsePendingRelevanceLowCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePendingRelevanceMediumCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=medium&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceMediumCategory1.getStatusCode());
    assertEquals(3, responsePendingRelevanceMediumCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceHighCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=high&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responsePendingRelevanceHighCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePendingRelevanceNoneCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=none&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responsePendingRelevanceNoneCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePendingRelevanceLowCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=low&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceLowCategory2.getStatusCode());
    assertEquals(3, responsePendingRelevanceLowCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePendingRelevanceMediumCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=medium&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responsePendingRelevanceMediumCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePendingRelevanceHighCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=pending&rel=high&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingRelevanceHighCategory2.getStatusCode());
    assertEquals(3, responsePendingRelevanceHighCategory2.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceNoneCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=none&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceNoneCategory1.getStatusCode());
    assertEquals(2, responseCompletedRelevanceNoneCategory1.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceLowCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=low&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responseCompletedRelevanceLowCategory1.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedRelevanceMediumCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=medium&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceMediumCategory1.getStatusCode());
    assertEquals(2, responseCompletedRelevanceMediumCategory1.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceHighCategory1 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=high&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responseCompletedRelevanceHighCategory1.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedRelevanceNoneCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=none&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responseCompletedRelevanceNoneCategory2.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedRelevanceLowCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=low&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceLowCategory2.getStatusCode());
    assertEquals(2, responseCompletedRelevanceLowCategory2.getBody().size());

    ResponseEntity<List<Task>> responseCompletedRelevanceMediumCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=medium&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responseCompletedRelevanceMediumCategory2.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedRelevanceHighCategory2 =
        testRestTemplate.exchange(
            BASE_URL + String.format("?status=completed&rel=high&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedRelevanceHighCategory2.getStatusCode());
    assertEquals(2, responseCompletedRelevanceHighCategory2.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceNoneCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=none&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceNoneCategory1.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceNoneCategory1.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceLowCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=low&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.NO_CONTENT, responseFuturePendingRelevanceLowCategory1.getStatusCode());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceMediumCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=medium&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceMediumCategory1.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceMediumCategory1.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceHighCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=high&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responseFuturePendingRelevanceHighCategory1.getStatusCode());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceNoneCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=none&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responseFuturePendingRelevanceNoneCategory2.getStatusCode());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceLowCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=low&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceLowCategory2.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceLowCategory2.getBody().size());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceMediumCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=medium&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responseFuturePendingRelevanceMediumCategory2.getStatusCode());

    ResponseEntity<List<Task>> responseFuturePendingRelevanceHighCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=future&rel=high&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseFuturePendingRelevanceHighCategory2.getStatusCode());
    assertEquals(1, responseFuturePendingRelevanceHighCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceNoneCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=previous&rel=none&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceNoneCategory1.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceNoneCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceLowCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=previous&rel=low&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousPendingRelevanceLowCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceMediumCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=pending&due=previous&rel=medium&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceMediumCategory1.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceMediumCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceHighCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=previous&rel=high&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousPendingRelevanceHighCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceNoneCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=previous&rel=none&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousPendingRelevanceNoneCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceLowCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=previous&rel=low&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceLowCategory2.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceLowCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceMediumCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=pending&due=previous&rel=medium&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousPendingRelevanceMediumCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousPendingRelevanceHighCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=previous&rel=high&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousPendingRelevanceHighCategory2.getStatusCode());
    assertEquals(1, responsePreviousPendingRelevanceHighCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceNoneCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=none&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceNoneCategory1.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceNoneCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceLowCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=low&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePendingForTodayRelevanceLowCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceMediumCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=medium&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceMediumCategory1.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceMediumCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceHighCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=high&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePendingForTodayRelevanceHighCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceNoneCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=none&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePendingForTodayRelevanceNoneCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceLowCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=low&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceLowCategory2.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceLowCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceMediumCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=medium&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePendingForTodayRelevanceMediumCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePendingForTodayRelevanceHighCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=pending&due=today&rel=high&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePendingForTodayRelevanceHighCategory2.getStatusCode());
    assertEquals(1, responsePendingForTodayRelevanceHighCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceNoneCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=none&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceNoneCategory1.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceNoneCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceLowCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=low&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousCompletedRelevanceLowCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceMediumCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=medium&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceMediumCategory1.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceMediumCategory1.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceHighCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=high&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousCompletedRelevanceHighCategory1.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceNoneCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=none&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousCompletedRelevanceNoneCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceLowCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=low&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceLowCategory2.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceLowCategory2.getBody().size());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceMediumCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=medium&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responsePreviousCompletedRelevanceMediumCategory2.getStatusCode());

    ResponseEntity<List<Task>> responsePreviousCompletedRelevanceHighCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=previous&rel=high&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responsePreviousCompletedRelevanceHighCategory2.getStatusCode());
    assertEquals(1, responsePreviousCompletedRelevanceHighCategory2.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceNoneCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=completed&due=today&rel=none&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceNoneCategory1.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceNoneCategory1.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceLowCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=completed&due=today&rel=low&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responseCompletedTodayRelevanceLowCategory1.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceMediumCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=today&rel=medium&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceMediumCategory1.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceMediumCategory1.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceHighCategory1 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=completed&due=today&rel=high&categoryId=%d", categoryId1),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responseCompletedTodayRelevanceHighCategory1.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceNoneCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=completed&due=today&rel=none&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responseCompletedTodayRelevanceNoneCategory2.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceLowCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=completed&due=today&rel=low&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceLowCategory2.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceLowCategory2.getBody().size());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceMediumCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format(
                    "?status=completed&due=today&rel=medium&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(
        HttpStatus.NO_CONTENT, responseCompletedTodayRelevanceMediumCategory2.getStatusCode());

    ResponseEntity<List<Task>> responseCompletedTodayRelevanceHighCategory2 =
        testRestTemplate.exchange(
            BASE_URL
                + String.format("?status=completed&due=today&rel=high&categoryId=%d", categoryId2),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseCompletedTodayRelevanceHighCategory2.getStatusCode());
    assertEquals(1, responseCompletedTodayRelevanceHighCategory2.getBody().size());

    ResponseEntity<List<Task>> responseFutureCompletedBadRequest =
        testRestTemplate.exchange(
            BASE_URL + "?status=completed&due=future",
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.BAD_REQUEST, responseFutureCompletedBadRequest.getStatusCode());

    testHelper.deleteTestUser();
  }

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
  void changeTaskStatus() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    UserEntity userEntity = userService.getUserByUsername(TestUser.USERNAME.value).orElseThrow();

    Long idPending =
        userEntity.getTasks().stream()
            .filter(task -> task.getCompletedDate() == null)
            .findFirst()
            .orElseThrow()
            .getTaskId();
    Long idCompleted =
        userEntity.getTasks().stream()
            .filter(task -> task.getCompletedDate() != null)
            .findFirst()
            .orElseThrow()
            .getTaskId();

    ResponseEntity<Task> response1 =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", idPending)), HttpMethod.PUT, request, Task.class);
    ResponseEntity<Task> response2 =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", idCompleted)),
            HttpMethod.PUT,
            request,
            Task.class);

    assertEquals(HttpStatus.OK, response1.getStatusCode());
    assertEquals(HttpStatus.OK, response2.getStatusCode());

    assertNull(response2.getBody().getCompletedDate());
    assertNotNull(response1.getBody().getCompletedDate());

    assertEquals(LocalDate.now(), response1.getBody().getCompletedDate());

    testHelper.deleteTestUser();
  }

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
