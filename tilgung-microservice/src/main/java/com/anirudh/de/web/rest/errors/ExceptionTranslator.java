package com.anirudh.de.web.rest.errors;


import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 *
 * @author Anirudh
 */
@ControllerAdvice(basePackages = {"com.anirudh.de"})
public class ExceptionTranslator {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CustomErrorResponse> handleResourceNotFound(final HttpMessageNotReadableException exception) {
    CustomErrorResponse error = createCustomErrorResponse(
      "The request payload has wrong format.",
      exception,
      HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<CustomErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException exception) {
    CustomErrorResponse error = createCustomErrorResponse(
      "The request payload has unexpected data type.",
      exception,
      HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = TilgungPlanCreationException.class)
  public ResponseEntity<CustomErrorResponse> handleEntitynotFound(TilgungPlanCreationException e) {
    CustomErrorResponse error = createCustomErrorResponse("NOT_FOUND_ERROR", e,
      HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  private CustomErrorResponse createCustomErrorResponse(String errorMessage, Exception e, HttpStatus status) {
    CustomErrorResponse error = new CustomErrorResponse(errorMessage, e.getMessage());
    error.setTimestamp(LocalDateTime.now());
    error.setStatus(status.value());
    return error;
  }
}
