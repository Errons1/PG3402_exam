package eu.voops.frontend.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class AccountExistException extends RuntimeException {
    private HttpStatus status;
    
    
    public AccountExistException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
}
