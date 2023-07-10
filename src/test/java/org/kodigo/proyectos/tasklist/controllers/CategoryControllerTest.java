package org.kodigo.proyectos.tasklist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.testutils.TestUser;
import org.kodigo.proyectos.tasklist.services.CategoryService;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.kodigo.proyectos.tasklist.testutils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
  private final String BASE_URL = "/categories";
  private TestRestTemplate testRestTemplate;
  @Autowired private RestTemplateBuilder restTemplateBuilder;
  @Autowired private CategoryService categoryService;
  @Autowired private UserService userService;
  @LocalServerPort private int port;
  @Autowired private TestHelper testHelper;

  @BeforeEach
  void setUp() {
    restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
    testRestTemplate = new TestRestTemplate(restTemplateBuilder);
  }

  @DisplayName("GET /categories")
  @Test
  void getCategories() {
    testHelper.registerTestUser(testRestTemplate);
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);
    ResponseEntity<List<Category>> response1 =
        testRestTemplate.exchange(
            BASE_URL, HttpMethod.GET, request, new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.NO_CONTENT, response1.getStatusCode());
    testHelper.createTestData();
    ResponseEntity<List<Category>> response2 =
        testRestTemplate.exchange(
            BASE_URL, HttpMethod.GET, request, new ParameterizedTypeReference<>() {});

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();
    List<Category> userEntityCategories = userEntity.getCategories();
    List<Category> responseBodyCategories = response2.getBody();
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    assertEquals(userEntityCategories.size(), responseBodyCategories.size());
    for (int i = 0; i < userEntityCategories.size(); i++) {
      assertEquals(userEntityCategories.get(i).getName(), responseBodyCategories.get(i).getName());
    }
    testHelper.deleteTestUser();
  }

  @DisplayName("POST /categories")
  @Test
  void createCategory() {
    testHelper.registerTestUser(testRestTemplate);
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);

    String body =
        String.format("{\"categoryId\": \"%d\",\"name\": \"%s\"}", 3, "category to create");
    String body2 =
        String.format("{\"categoryId\": \"%d\",\"name\": \"%s\"}", 4, "category to create");
    HttpEntity<String> request = new HttpEntity<>(body, headers);
    HttpEntity<String> request2 = new HttpEntity<>(body2, headers);
    ResponseEntity<Category> response =
        testRestTemplate.exchange(BASE_URL, HttpMethod.POST, request, Category.class);
    ResponseEntity<Category> response2 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.POST, request2, Category.class);
    Category category = response.getBody();
    Category category2 = response2.getBody();

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    assertNull(category2);
    assertEquals("category to create", category.getName());
    testHelper.deleteTestUser();
  }

  @DisplayName("PUT /categories")
  @Test
  void modifyCategory() {
    testHelper.registerTestUser(testRestTemplate);
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);

    testHelper.createTestData();

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();
    Category category1 =
        categoryService.getCategoryByName(userEntity, "Test category 1").orElseThrow();
    Category category2 =
        categoryService.getCategoryByName(userEntity, "Test category 2").orElseThrow();

    String body1 = String.format("{\"name\": \"%s\"}", "Test category changed");
    String body2 =
        String.format(
            "{\"categoryId\": \"%d\",\"name\": \"%s\"}",
            category1.getCategoryId(), "Test category 2");
    String body3 =
        String.format(
            "{\"categoryId\": \"%d\",\"name\": \"%s\"}",
            category2.getCategoryId() + 1L, "Test category changed");
    String body4 =
        String.format(
            "{\"categoryId\": \"%d\",\"name\": \"%s\"}",
            category1.getCategoryId(), "Test category changed");

    HttpEntity<String> request1 = new HttpEntity<>(body1, headers);
    HttpEntity<String> request2 = new HttpEntity<>(body2, headers);
    HttpEntity<String> request3 = new HttpEntity<>(body3, headers);
    HttpEntity<String> request4 = new HttpEntity<>(body4, headers);

    ResponseEntity<Category> response1 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, request1, Category.class);
    assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

    ResponseEntity<Category> response2 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, request2, Category.class);
    assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());

    ResponseEntity<Category> response3 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, request3, Category.class);
    assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());

    ResponseEntity<Category> response4 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.PUT, request4, Category.class);
    assertEquals(HttpStatus.OK, response4.getStatusCode());

    Optional<Category> categoryOpt =
        categoryService.getCategoryByName(userEntity, "Test category changed");
    assertTrue(categoryOpt.isPresent());
    assertEquals(category1.getCategoryId(), categoryOpt.orElseThrow().getCategoryId());

    testHelper.deleteTestUser();
  }

  @DisplayName("DELETE /categories")
  @Test
  void deleteCategories() {
    testHelper.registerTestUser(testRestTemplate);
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);
    ResponseEntity<Category> response1 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.DELETE, request, Category.class);

    testHelper.createTestData();
    ResponseEntity<Category> response2 =
        testRestTemplate.exchange(BASE_URL, HttpMethod.DELETE, request, Category.class);

    UserEntity userEntity = userService.getUserByEmail(TestUser.EMAIL.value).orElseThrow();

    Optional<Category> category1 = categoryService.getCategoryByName(userEntity, "Test category 1");
    Optional<Category> category2 = categoryService.getCategoryByName(userEntity, "Test category 2");

    assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
    assertEquals(HttpStatus.NO_CONTENT, response2.getStatusCode());
    assertTrue(category1.isEmpty());
    assertTrue(category2.isEmpty());

    testHelper.deleteTestUser();
  }

  @DisplayName("GET /categories/{categoryId}")
  @Test
  void getCategoryById() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    UserEntity userEntity = userService.getUserByUsername(TestUser.USERNAME.value).orElseThrow();
    Category categoryToSearch =
        categoryService.getCategoryByName(userEntity, "Test category 1").orElseThrow();
    Long id = categoryToSearch.getCategoryId();

    ResponseEntity<Category> response =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", id)), HttpMethod.GET, request, Category.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(categoryToSearch.getName(), response.getBody().getName());

    testHelper.deleteTestUser();
  }

  @DisplayName("DELETE /categories/{categoryId}")
  @Test
  void deleteCategory() {
    testHelper.registerTestUser(testRestTemplate);
    testHelper.createTestData();
    HttpHeaders headers = testHelper.getHeadersWithAuthenticationForTestUser(testRestTemplate);
    HttpEntity<String> request = new HttpEntity<>(null, headers);

    UserEntity userEntity = userService.getUserByUsername(TestUser.USERNAME.value).orElseThrow();
    Category categoryToDelete =
        categoryService.getCategoryByName(userEntity, "Test category 2").orElseThrow();
    Long id = categoryToDelete.getCategoryId();
    Long fakeId = id + 1L;

    ResponseEntity<Category> response1 =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", fakeId)),
            HttpMethod.DELETE,
            request,
            Category.class);

    ResponseEntity<Category> response2 =
        testRestTemplate.exchange(
            BASE_URL.concat(String.format("/%d", id)), HttpMethod.DELETE, request, Category.class);

    assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
    assertEquals(HttpStatus.NO_CONTENT, response2.getStatusCode());

    Optional<Category> deletedCategory =
        categoryService.getCategoryByName(userEntity, "Test category 2");

    assertTrue(deletedCategory.isEmpty());
    testHelper.deleteTestUser();
  }
}
