package org.kodigo.proyectos.tasklist.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;
import org.kodigo.proyectos.tasklist.entities.Relevance;
import org.kodigo.proyectos.tasklist.entities.Task;

public class PDFTableGenerator {

  private final PdfPTable table;

  public PDFTableGenerator(String[] headers, float[] columnWidth) throws DocumentException {
    table = new PdfPTable(headers.length);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
    BaseColor customColor = BaseColor.ORANGE;
    table.setWidths(columnWidth);

    for (String header : headers) {
      PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
      headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      headerCell.setBackgroundColor(customColor);
      table.addCell(headerCell);
    }
  }

  public PdfPTable fillTaskTablesByRelevance(List<Task> tasks, Relevance relevance) {
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6);
    for (Task task : tasks) {
      if (task.getRelevance() == relevance) {
        PdfPCell headerCell = new PdfPCell(new Phrase(task.getName(), headerFont));
        PdfPCell headerCell1 =
            new PdfPCell(new Phrase(task.getCompletedDate().toString(), headerFont));
        PdfPCell headerCell3 = new PdfPCell(new Phrase(task.getDescription(), headerFont));
        PdfPCell headerCell4 =
            new PdfPCell(
                new Phrase(
                    task.getCategory() != null ? task.getCategory().getName() : "N/A", headerFont));
        table.addCell(headerCell);
        table.addCell(headerCell1);
        table.addCell(headerCell3);
        table.addCell(headerCell4);
      }
    }
    return table;
  }
}
