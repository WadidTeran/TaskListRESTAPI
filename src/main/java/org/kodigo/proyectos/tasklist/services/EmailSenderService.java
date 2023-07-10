package org.kodigo.proyectos.tasklist.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
public class EmailSenderService {

  private final String senderEmail;
  private final MimeMessageHelper mimeMessageHelper;
  private final MimeMessage message;
  private final JavaMailSender javaMailSender;

  public EmailSenderService(@Autowired JavaMailSender javaMailSender) throws MessagingException {
    this.senderEmail = "listtaskapp@gmail.com";
    this.javaMailSender = javaMailSender;
    this.message = javaMailSender.createMimeMessage();
    this.mimeMessageHelper = new MimeMessageHelper(message, true);
  }

  public boolean sendEmail(String subject, String textBody, String receiverEmail) {
    try {
      mimeMessageHelper.setFrom(senderEmail);
      mimeMessageHelper.setTo(receiverEmail);
      mimeMessageHelper.setText(textBody);
      mimeMessageHelper.setSubject(subject);
      javaMailSender.send(message);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean sendEmailWithFile(
      String subject, String textBody, String fileDataSource, String receiverEmail) {
    try {
      mimeMessageHelper.setFrom(senderEmail);
      mimeMessageHelper.setTo(receiverEmail);
      mimeMessageHelper.setText(textBody);
      mimeMessageHelper.setSubject(subject);
      File file = new File(fileDataSource);
      if (file.exists()) {
        mimeMessageHelper.addAttachment(file.getName(), file);
      } else {
        return false;
      }
      javaMailSender.send(message);
      Files.delete(file.toPath());
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
