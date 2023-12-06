package eu.voops.authentication.exception;

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
    
    @ExceptionHandler(AccountExistException.class)
    public ResponseEntity<String> accountExistResponse(AccountExistException e) {
        log.warn(e.getMessage());
        String response = "Error " + e.getStatus() + ": " + e.getMessage();
        return new ResponseEntity<>(response, e.getStatus());
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<String> methodArgumentNotValidException(RuntimeException e) {
//        log.warn(e.getMessage());
//        String response = "Error 400: Bad Request";
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

}
