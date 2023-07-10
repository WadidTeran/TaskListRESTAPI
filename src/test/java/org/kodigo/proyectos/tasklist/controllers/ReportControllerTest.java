package org.kodigo.proyectos.tasklist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.testutils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportControllerTest {
  private final String BASE_URL = "/reports";
  private TestRestTemplate testRestTemplate;
  @Autowired private RestTemplateBuilder restTemplateBuilder;
  @LocalServerPort private int port;
  @Autowired private TestHelper testHelper;

  @BeforeEach
  void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
    testRestTemplate = new TestRestTemplate(restTemplateBuilder);
  }

  @DisplayName("GET /reports")
  @Test
  void sendProductivityEmail() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    LocalDate future = LocalDate.now().plusYears(10);
    LocalDate past = LocalDate.now().minusYears(1);

    ResponseEntity<?> responseA =
        testRestTemplate.exchange(
            BASE_URL + String.format("?from=%s&to=%s", LocalDate.now(), future),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    ResponseEntity<?> responseB =
        testRestTemplate.exchange(
            BASE_URL + String.format("?from=%s&to=%s", LocalDate.now(), past),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    ResponseEntity<?> responseC =
        testRestTemplate.exchange(
            BASE_URL + String.format("?from=%s&to=%s", future, future.plusYears(1)),
            HttpMethod.GET,
            request,
            new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, responseA.getStatusCode());
    assertEquals(HttpStatus.BAD_REQUEST, responseB.getStatusCode());
    assertEquals(HttpStatus.OK, responseC.getStatusCode());

    testHelper.deleteTestUser();
  }
}
