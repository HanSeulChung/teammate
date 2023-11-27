package com.api.backend.global.exception;

import com.api.backend.global.exception.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException {

  private ErrorCode errorCode;
  private String errorMessage;

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getErrorMessage();
  }
}
