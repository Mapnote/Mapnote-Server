package com.mapnote.mapnoteserver.domain.common.exception;

import com.mapnote.mapnoteserver.domain.common.dto.ErrorResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionAdvice {

  private final SlackApi slackApi;

  public ExceptionAdvice(SlackApi slackApi) {
    this.slackApi = slackApi;
  }

  // @Validated - binding error
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
    ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return ResponseEntity.badRequest().body(response);
  }

  // enum type 일치 하지 않아 binding 못하는 경우 발생
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    ErrorResponse response = ErrorResponse.of(e);
    return ResponseEntity.badRequest().body(response);
  }

  // 지원하지 않은 HTTP method 호출 할 경우 발생
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = ErrorResponse.of(errorCode);
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(HttpServletRequest req, Exception e) {
    ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    sendSlackMessage(req, e);
    return ResponseEntity.internalServerError().body(response);
  }

  private void sendSlackMessage(HttpServletRequest req, Exception e) {
    SlackAttachment slackAttachment = new SlackAttachment();

    slackAttachment.setFallback("Error");
    slackAttachment.setColor("danger");
    slackAttachment.setTitle("⚠️ ERROR DETECT");
    slackAttachment.setTitleLink(req.getContextPath());
    slackAttachment.setText(e.getStackTrace().toString());
    slackAttachment.setColor("danger");
    slackAttachment.setFields(getSlackField(req));

    SlackMessage slackMessage = new SlackMessage();
    slackMessage.setAttachments(Collections.singletonList(slackAttachment));
    slackMessage.setIcon(":ghost:");
    slackMessage.setText("⚠️ SERVER EXCEPTION DETECT");
    slackMessage.setUsername("Server Exception!!");

    slackApi.call(slackMessage);
  }

  private List getSlackField (HttpServletRequest req) {

    List<SlackField> fields = new ArrayList<>();

    fields.add(new SlackField().setTitle("Request URL").setValue(req.getRequestURI().toString()));
    fields.add(new SlackField().setTitle("Request Method").setValue(req.getMethod()));
    fields.add(new SlackField().setTitle("Request Time").setValue(new Date().toString()));
    fields.add(new SlackField().setTitle("Request IP").setValue(req.getRemoteUser()));

    return fields;
  }

}
