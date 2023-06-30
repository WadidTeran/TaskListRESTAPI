package org.kodigo.proyectos.tasklist.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import lombok.Getter;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;
import org.kodigo.proyectos.tasklist.utils.ChartGenerator;
import org.kodigo.proyectos.tasklist.utils.PDFTableGenerator;
import org.kodigo.proyectos.tasklist.utils.RangeDates;

import static org.kodigo.proyectos.tasklist.utils.ExternalUtilityMethods.countTaskByRelevance;

public class PDFGeneratorService {
  private final Document document;
  @Getter private final String nameDocument;

  public PDFGeneratorService(String nameDocument) {
    document = new Document();
    this.nameDocument = "./" + nameDocument;
  }

  public static Paragraph generateTitle(String text) {
    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    Paragraph title = new Paragraph(text, titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    title.setSpacingAfter(10);
    title.setSpacingBefore(30);
    return title;
  }

  public void generateBasicPDF(String title, String textBody) {

    try {
      String path = new File(".").getCanonicalPath();
      String fileName = path + nameDocument;

      PdfWriter.getInstance(document, new FileOutputStream(fileName));

      document.open();
      Paragraph pdfTitle = new Paragraph(title);
      pdfTitle.setAlignment(Element.ALIGN_JUSTIFIED);

      Paragraph body = new Paragraph(textBody);

      Paragraph p3 = new Paragraph("Task-List");

      Font f = new Font();
      f.setFamily(Font.FontFamily.COURIER.name());
      f.setStyle(Font.BOLDITALIC);
      f.setSize(8);

      p3.setFont(f);
      f.setSize(10);

      document.add(pdfTitle);
      document.add(body);
      document.add(p3);
      document.close();
    } catch (Exception ex) {
      Logger.getLogger(PDFGeneratorService.class.getName())
          .log(Level.SEVERE, " Error trying to generate a document ", ex);
    }
  }

  public void generateProductivityPDF(List<Task> tasks, RangeDates rangeDates) {

    try {
      String path = new File(nameDocument).getCanonicalPath();
      String[] headersRelevance = {"NAME", "COMPLETED DATE", "DESCRIPTION", "CATEGORY"};
      String[] titlesChart = {"PRODUCTIVITY GRAPH", "Relevance", "Completed tasks"};
      float[] widthsRelevance = {30f, 15f, 30f, 25f};

      PdfWriter.getInstance(document, new FileOutputStream(path));
      document.open();

      Paragraph dateRange =
          new Paragraph(
              rangeDates.startDate().isEqual(rangeDates.endDate())
                  ? "Of " + rangeDates.endDate()
                  : "From " + rangeDates.startDate() + " to " + rangeDates.endDate());
      dateRange.setAlignment(Element.ALIGN_RIGHT);
      dateRange.setSpacingAfter(30);

      Paragraph title = new Paragraph("Productivity report");
      title.setAlignment(Element.ALIGN_JUSTIFIED);

      Paragraph signature = new Paragraph("Task-List " + LocalDate.now());
      signature.setAlignment(Element.ALIGN_RIGHT);

      document.add(title);
      document.add(dateRange);
      document.add(
          ChartGenerator.generateBarChart(
              titlesChart, ChartGenerator.generateRelevanceDataset(tasks)));
      File chartImageFile = new File("chart.png");
      if (chartImageFile.exists()) {
        chartImageFile.deleteOnExit();
      }

      int[] relevanceTaskCount = countTaskByRelevance(tasks);
      for (int i = 0; i < relevanceTaskCount.length; i++) {
        if (relevanceTaskCount[i] > 0) {
          PDFTableGenerator table = new PDFTableGenerator(headersRelevance, widthsRelevance);
          document.add(generateTitle("COMPLETED TASKS BY " + Relevance.values()[i] + " RELEVANCE"));
          document.add(table.fillTaskTablesByRelevance(tasks, Relevance.values()[i]));
        }
      }
      document.add(signature);
      document.close();
    } catch (Exception ex) {
      Logger.getLogger(PDFGeneratorService.class.getName())
          .log(Level.SEVERE, " Error trying to generate a document ", ex);
    }
  }
}
