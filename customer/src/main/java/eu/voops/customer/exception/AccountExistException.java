package eu.voops.customer.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountExistException extends RuntimeException {
    public AccountExistException(String message) {
        super(message);
    }
}

