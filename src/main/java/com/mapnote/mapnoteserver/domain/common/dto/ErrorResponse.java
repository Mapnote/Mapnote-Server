package com.mapnote.mapnoteserver.domain.common.dto;

import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse extends BaseResponse{

  private List<ErrorField> errors;

  private ErrorResponse(ErrorCode errorCode, List<ErrorField> errors) {
    super(errorCode);
    this.errors = errors;
  }

  private ErrorResponse(ErrorCode errorCode) {
    super(errorCode);
    this.errors = new ArrayList<>();
  }

  public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
    return new ErrorResponse(code, ErrorField.of(bindingResult));
  }

  public static ErrorResponse of(final ErrorCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(final ErrorCode code, final List<ErrorField> errors) {
    return new ErrorResponse(code, errors);
  }

  public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
    final String value = e.getValue() == null ? "" : e.getValue().toString();
    final List<ErrorResponse.ErrorField> errors = ErrorResponse.ErrorField.of(e.getName(), value, e.getErrorCode());
    return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ErrorField {

    private String field;
    private String value;
    private String reason;

    public ErrorField(String field, String value, String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public static List<ErrorField> of(String field, String value, String reason) {
      List<ErrorField> fieldErrors = new ArrayList<>();
      fieldErrors.add(new ErrorField(field, value, reason));
      return fieldErrors;
    }

    private static List<ErrorField> of(BindingResult bindingResult) {
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
          .map(error -> new ErrorField(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }
}
