package eu.voops.authentication;

import eu.voops.authentication.exception.AccountExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class AuthenticationService {

    private AuthenticationRepository repository;

    public void createAccount(Authentication authentication) {
        if (repository.existsByInternalId(authentication.getInternalId())) {
            throw new AccountExistException("Account already exist");
        }

        repository.save(authentication);
    }

    @Transactional
    public void emergencyDelete(String internalId) {
        repository.deleteByInternalId(internalId);
    }
}
