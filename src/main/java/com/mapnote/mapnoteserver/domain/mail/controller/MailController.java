package com.mapnote.mapnoteserver.domain.mail.controller;

import com.mapnote.mapnoteserver.domain.common.dto.ErrorResponse;
import com.mapnote.mapnoteserver.domain.mail.dto.MailRequest;
import com.mapnote.mapnoteserver.domain.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "mail", description = "메일 API")
@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

  private final MailService mailService;

  public MailController(MailService mailService) {
    this.mailService = mailService;
  }

  @Operation(summary = "send Mail", description = "이메일 전송 요청")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/{type}")
  public ResponseEntity<String> certificationSignup(@Validated @RequestBody MailRequest mailRequest, @PathVariable String type) {
    String authenticationString = mailService.sendMail(mailRequest, type);
    return ResponseEntity.ok(authenticationString);
  }

}
