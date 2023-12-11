package eu.voops.account.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericResponse(RuntimeException e) {
        log.warn(e.getMessage());
        String response = "Error 500: Internal Server error";
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<String> error400(RuntimeException e) {
        log.warn(e.getMessage());
        String response = "Error 400: Bad request\n" + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}