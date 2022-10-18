package com.mapnote.mapnoteserver.domain.mail.service;

import com.mapnote.mapnoteserver.domain.mail.dto.MailRequest;
import com.mapnote.mapnoteserver.domain.mail.util.AuthenticationString;
import com.mapnote.mapnoteserver.domain.mail.util.MailBody;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PropertySource("classpath:application.yml")
@Transactional(readOnly = true)
public class MailService {

  @Value("${spring.mail.username}")
  private String from;

  private final JavaMailSender mailSender;

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Transactional
  public String sendMail(MailRequest mailRequest) {

    String authenticationString = AuthenticationString.generateRandomString();

    String to = mailRequest.getEmail();

    String title = MailBody.signupMailTitle();
    String content = MailBody.signupMailBody(authenticationString);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

      messageHelper.setFrom(from);
      messageHelper.setTo(to);
      messageHelper.setSubject(title);
      messageHelper.setText(content, true);

      mailSender.send(message);

    } catch (MessagingException e) {
      e.printStackTrace();
    }

    return authenticationString;
  }
}
