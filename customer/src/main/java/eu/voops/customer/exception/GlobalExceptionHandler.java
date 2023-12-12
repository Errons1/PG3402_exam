package eu.voops.customer.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.rmi.NoSuchObjectException;
import java.util.NoSuchElementException;

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

    @ExceptionHandler(ProfileExistException.class)
    public ResponseEntity<String> error409(RuntimeException e) {
        log.warn(e.getMessage());
        String response = "Error 409: Conflict\n" + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> error404(RuntimeException e) {
        log.warn(e.getMessage());
        String response = "Error 404: NOT FOUND\n" + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
}