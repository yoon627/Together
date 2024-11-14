package com.zerobase.together.exception;

import static com.zerobase.together.type.ErrorCode.INTERNAL_SERVER_ERROR;

import com.zerobase.together.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    log.error("{} is occurred", e.getErrorCode());
    return new ResponseEntity<>(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthorityException.class)
  public ResponseEntity<ErrorResponse> handleAuthorityException(AuthorityException e) {
    log.error("{} is occurred", e.getErrorCode());
    return new ResponseEntity<>(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Error is occurred", e);
    return new ResponseEntity<>(new ErrorResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR
        .getDescription()), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
