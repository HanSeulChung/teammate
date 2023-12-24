package com.api.backend.global.exception;

import com.api.backend.global.exception.dto.ErrorResponse;
import com.api.backend.global.exception.type.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ExceptionController {


  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    return ResponseEntity
        .badRequest()
        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
    ErrorCode errorCode = ErrorCode.IMG_FILE_VOLUMNE_TOO_BIG_EXCEPTION;
    return ResponseEntity
        .badRequest()
        .body(new ErrorResponse(errorCode, errorCode.getErrorMessage()));
  }


}