package eu.voops.authentication;

import eu.voops.authentication.exception.ProfileExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@AllArgsConstructor
@Service
public class AuthenticationService {

    private AuthenticationRepository repository;

    public void createAccount(Authentication authentication) {
        if (repository.existsByInternalId(authentication.getInternalId())) {
            throw new ProfileExistException("Account already exist");
        }

        repository.save(authentication);
    }

    @Transactional
    public void emergencyDelete(String internalId) {
        if (repository.existsByInternalId(internalId)) {
            repository.deleteByInternalId(internalId);
        } else{
            throw new NoSuchElementException("Profile does not exist");
        }
    }
}
