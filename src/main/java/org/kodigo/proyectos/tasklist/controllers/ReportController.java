package org.kodigo.proyectos.tasklist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.kodigo.proyectos.tasklist.controllers.utils.ControllerUtils;
import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.services.ProductivityEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

  @Autowired private ControllerUtils controllerUtils;
  @Autowired private ProductivityEmailService productivityEmailService;

  @GetMapping
  @Operation(summary = "Send productivity email")
  public ResponseEntity<?> sendProductivityEmail(
      @RequestHeader HttpHeaders headers,
      @Parameter(description = "Start date for productivity report range") @RequestParam("from")
          String startDate,
      @Parameter(description = "End date for productivity report range") @RequestParam("to")
          String endDate) {
    UserEntity user = controllerUtils.getUserEntityFromHeaders(headers);
    return productivityEmailService.sendProductivityEmail(user, startDate, endDate)
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().build();
  }
}
