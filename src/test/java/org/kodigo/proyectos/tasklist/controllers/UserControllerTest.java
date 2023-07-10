package org.kodigo.proyectos.tasklist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.testutils.TestUser;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.kodigo.proyectos.tasklist.testutils.TestHelper;
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
  @Autowired private TestHelper testHelper;

  @BeforeEach
  void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
    testRestTemplate = new TestRestTemplate(restTemplateBuilder);
  }

  @DisplayName("GET /account")
  @Test
  void getUserEntity() {
    testHelper.registerTestUser(testRestTemplate);
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);
    ResponseEntity<UserEntity> response =
        testRestTemplate.exchange(BASE_URL, HttpMethod.GET, request, UserEntity.class);

    UserEntity userEntity = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(TestUser.EMAIL.value, userEntity.getEmail());
    assertEquals(TestUser.USERNAME.value, userEntity.getUsername());
    testHelper.deleteTestUser();
  }

  @DisplayName("POST /account")
  @Test
  void modifyAccount() {
    testHelper.registerTestUser(testRestTemplate);
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();
    String body1 =
        String.format(
            "{\"userId\": \"%d\",\"email\": \"%s\", \"username\": \"%s\", \"password\": \"%s\"}",
            userEntity.getUserId(),
            "newemail@test.com",
            TestUser.USERNAME.value,
            TestUser.PASSWORD.value);
    String body2 =
            String.format(
                    "{\"userId\": \"%d\",\"email\": \"%s\", \"username\": \"%s\", \"password\": \"%s\"}",
                    userEntity.getUserId() + 1L,
                    TestUser.EMAIL.value,
                    TestUser.USERNAME.value,
                    TestUser.PASSWORD.value);
    String body3 =
        String.format(
            "{\"userId\": \"%d\",\"email\": \"%s\", \"username\": \"%s\", \"password\": \"%s\"}",
            userEntity.getUserId(), TestUser.EMAIL.value, "new_username", TestUser.PASSWORD.value);

    HttpEntity<String> request1 = new HttpEntity<>(body1, headers);
    HttpEntity<String> request2 = new HttpEntity<>(body2, headers);
    HttpEntity<String> request3 = new HttpEntity<>(body3, headers);

    ResponseEntity<UserEntity> response1 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.POST, request1, UserEntity.class);
    Optional<UserEntity> userOpt = userService.getUserByEmail("newemail@test.com");
    ResponseEntity<UserEntity> response2 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.POST, request2, UserEntity.class);
    ResponseEntity<UserEntity> response3 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.POST, request3, UserEntity.class);
    Optional<UserEntity> user2Opt = userService.getUserByUsername("new_username");

    assertEquals(HttpStatus.OK, response1.getStatusCode());
    assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    assertTrue(userOpt.isPresent());
    assertTrue(user2Opt.isPresent());

    userService.deleteUser(user2Opt.get());
  }

  @DisplayName("DELETE /account")
  @Test
  void deleteAccount() {
    testHelper.registerTestUser(testRestTemplate);
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);
    ResponseEntity<UserEntity> response =
        testRestTemplate.exchange(BASE_URL, HttpMethod.DELETE, request, UserEntity.class);

    Optional<UserEntity> userEntity = userService.getUserByEmail(TestUser.EMAIL.value);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertTrue(userEntity.isEmpty());
  }

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
