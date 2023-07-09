package org.kodigo.proyectos.tasklist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
  private final String BASE_URL = "/account";
  private final String REGISTER_URL = "/register";
  private TestRestTemplate testRestTemplate;
  @Autowired private RestTemplateBuilder restTemplateBuilder;
  @Autowired private UserService userService;
  @LocalServerPort private int port;

  @BeforeEach
  void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
    testRestTemplate = new TestRestTemplate(restTemplateBuilder);
  }

  @DisplayName("GET /account")
  @Test
  void getUserEntity() {}

  @DisplayName("PATCH /account")
  @Test
  void modifyAccount() {}

  @DisplayName("DELETE /account")
  @Test
  void deleteAccount() {}

  @DisplayName("POST /register")
  @Test
  void register() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    String body1 =
        """
{
    "email": "test@gmail.com",
    "username": "test",
    "password": "89wvn7YNFB0YX@"
}
""";
    String body2 =
        """
{
    "email": "test@gmail.com",
    "username": "testuser2",
    "password": "Aax2,.*asiond#2@"
}
""";

    HttpEntity<String> request = new HttpEntity<>(body1, headers);
    HttpEntity<String> request2 = new HttpEntity<>(body2, headers);

    ResponseEntity<UserEntity> response1 =
        testRestTemplate.exchange(REGISTER_URL, HttpMethod.POST, request, UserEntity.class);
    ResponseEntity<UserEntity> response2 =
        testRestTemplate.exchange(REGISTER_URL, HttpMethod.POST, request2, UserEntity.class);

    assertEquals(HttpStatus.OK, response1.getStatusCode());
    assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());

    Optional<UserEntity> userEntity = userService.getUserByEmail("test@gmail.com");

    assertTrue(userEntity.isPresent());
    assertEquals("test", userEntity.orElseThrow().getUsername());

    userService.deleteUser(userEntity.orElseThrow());
  }
}