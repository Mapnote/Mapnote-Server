package com.mapnote.mapnoteserver.domain.mail.controller;

import com.mapnote.mapnoteserver.domain.mail.dto.MailRequest;
import com.mapnote.mapnoteserver.domain.mail.service.MailService;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

  private final MailService mailService;

  public MailController(MailService mailService) {
    this.mailService = mailService;
  }

  @PostMapping("/signup")
  public ResponseEntity<String> certificationSignup(@Validated @RequestBody MailRequest mailRequest) {
    String authenticationString = mailService.sendMail(mailRequest);
    return ResponseEntity.ok(authenticationString);
  }

}
