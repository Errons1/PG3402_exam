package eu.voops.transfer.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProfileExistException extends RuntimeException {
    public ProfileExistException(String message) {
        super(message);
    }
}
