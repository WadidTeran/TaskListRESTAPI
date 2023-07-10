package org.kodigo.proyectos.tasklist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kodigo.proyectos.tasklist.entities.Category;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.security.jwt.utils.TestUser;
import org.kodigo.proyectos.tasklist.services.CategoryService;
import org.kodigo.proyectos.tasklist.services.UserService;
import org.kodigo.proyectos.tasklist.testutils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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

  @Test
  void getCategories() {}

  @Test
  void createCategory() {}

  @Test
  void modifyCategory() {}

  @Test
  void deleteCategories() {}

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

    assertEquals(categoryToSearch.getName(), response.getBody().getName());

    testHelper.deleteTestUser();
  }

  @Test
  void deleteCategory() {}
}
