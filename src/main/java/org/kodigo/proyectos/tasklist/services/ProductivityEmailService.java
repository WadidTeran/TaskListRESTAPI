package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.entities.User;
import org.kodigo.proyectos.tasklist.dtos.RangeDates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProductivityEmailService {

  @Autowired private EmailSenderService emailSenderService;
  @Autowired private TaskService taskService;

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

  public boolean sendProductivityEmail(User user, String startDateStr, String endDateStr) {
    Optional<RangeDates> optRangeDates = convertToRangeDates(startDateStr, endDateStr);
    if (optRangeDates.isPresent()) {
      try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
        Runnable emailSendingProcess = () -> sendingRunnable(user, optRangeDates.get());
        executor.execute(emailSendingProcess);
        return true;
      }
    }

    return false;
  }

  private void sendingRunnable(User user, RangeDates rangeDates) {
    List<Task> tasks =
        filterCompletedTasksByDate(taskService.getAllCompletedTasks(user), rangeDates);

    if (!tasks.isEmpty()) {
      PDFGeneratorService reportDocument = new PDFGeneratorService("Productivity report.pdf");
      reportDocument.generateProductivityPDF(tasks, rangeDates);
      emailSenderService.sendEmailWithFile(
          "Productivity email",
          "Hi, here is your productivity report :)",
          reportDocument.getNameDocument(),
          user.getEmail());
    } else {
      emailSenderService.sendEmail(
          "Productivity email",
          "You don't have any completed tasks in the provided range",
          user.getEmail());
    }
  }

  private Optional<RangeDates> convertToRangeDates(String startDateStr, String endDateStr) {
    if (startDateStr == null
        || startDateStr.isBlank()
        || startDateStr.isEmpty()
        || endDateStr == null
        || endDateStr.isBlank()
        || endDateStr.isEmpty()) {
      return Optional.empty();
    }

    try {
      LocalDate startDate = LocalDate.parse(startDateStr);
      LocalDate endDate = LocalDate.parse(endDateStr);
      if (!startDate.isAfter(endDate)) {
        RangeDates rangeDates = new RangeDates(startDate, endDate);
        return Optional.of(rangeDates);
      }
      return Optional.empty();
    } catch (DateTimeParseException e) {
      return Optional.empty();
    }
  }
}
