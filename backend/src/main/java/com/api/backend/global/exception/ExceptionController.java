package com.api.backend.global.exception;

import com.api.backend.global.exception.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

//  @ExceptionHandler(CustomException.class)
//  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
//    return ResponseEntity.badRequest()
//        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()));
//  }

  @ExceptionHandler(CustomException.class)
  public ErrorResponse handleCustomException(CustomException e) {
    return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
  }
}