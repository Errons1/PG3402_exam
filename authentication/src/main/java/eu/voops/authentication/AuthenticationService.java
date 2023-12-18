package eu.voops.authentication;

import eu.voops.authentication.dto.DtoLogin;
import eu.voops.authentication.entity.Authentication;
import eu.voops.authentication.exception.ProfileExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationRepository repository;

    public void createAccount(@NonNull Authentication authentication) {
        if (repository.existsByInternalId(authentication.getInternalId())) {
            throw new ProfileExistException("Account already exist");
        }

        repository.save(authentication);
    }

    @Transactional
    public void emergencyDelete(String internalId) {
        if (repository.existsByInternalId(internalId)) {
            repository.deleteByInternalId(internalId);
        } else {
            throw new NoSuchElementException("Profile does not exist");
        }
    }

    public boolean login(@NonNull DtoLogin dto) {
        if (repository.existsByPersonalId(dto.getPersonalId())) {
            Authentication authentication = repository.findByPersonalId(dto.getPersonalId());
            byte[] hash = Hash.sha256(dto.getPassword());
            return Arrays.equals(authentication.getPasswordHash(), hash);
        } else {
            return false;
        }
    }
    
}
