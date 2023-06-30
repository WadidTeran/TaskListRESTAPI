package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.utils.RangeDates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProductivityEmailService {

  @Autowired private EmailSenderService emailSenderService;
  @Autowired private TaskService taskService;

  public boolean askForRange(User user, String startDateStr, String endDateStr) {
    // Start date (yyyy-MM-dd)
    if (startDateStr == null
        || startDateStr.isBlank()
        || startDateStr.isEmpty()
        || endDateStr == null
        || endDateStr.isBlank()
        || endDateStr.isEmpty()) {
      return false;
      // EMPTY_INPUT
    }

    try {
      LocalDate startDate = LocalDate.parse(startDateStr);
      LocalDate endDate = LocalDate.parse(endDateStr);
      if (!startDate.isAfter(endDate)) {
        RangeDates rangeDates = new RangeDates(startDate, endDate);
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
          Runnable emailSendingProcess = () -> sendProductivityEmail(user, rangeDates);
          executor.execute(emailSendingProcess);
          return true;
        }
      } else {
        // Start date must be before end date!
        return false;
      }
    } catch (DateTimeParseException e) {
      // Invalid date format
      return false;
    }
  }

  public boolean sendProductivityEmail(User user, RangeDates rangeDates) {
    List<Task> tasks =
        filterCompletedTasksByDate(taskService.getAllCompletedTasks(user), rangeDates);
    if (!tasks.isEmpty()) {

      PDFGeneratorService reportDocument = new PDFGeneratorService("Productivity report.pdf");
      reportDocument.generateProductivityPDF(tasks, rangeDates);
      emailSenderService.sendEmailWithFile(
          "Productivity email",
          "Hi, here is your monthly productivity report :)",
          reportDocument.getNameDocument(),
          tasks.get(0).getUser().getEmail());
      return true;
    } else {
      return false;
    }
  }

  private static List<Task> filterCompletedTasksByDate(List<Task> tasks, RangeDates range) {
    return tasks.stream()
        .filter(
            t ->
                t.getCompletedDate().isAfter(range.startDate())
                        && t.getCompletedDate().isBefore(range.endDate())
                    || t.getCompletedDate().equals(range.startDate())
                    || t.getCompletedDate().equals(range.endDate()))
        .toList();
  }
}