package org.kodigo.proyectos.tasklist.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI taskListRestApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("TaskList")
                .description("Task List, Spring Boot REST API.")
                .version("v1.0"))
        .externalDocs(
            new ExternalDocumentation()
                .description("GitHub Repository")
                .url("https://github.com/WadidTeran/TaskListRESTAPI"));
  }
}
