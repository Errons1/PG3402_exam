package eu.voops.authentication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountExistException extends RuntimeException {
    private final HttpStatus status;

    public AccountExistException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
