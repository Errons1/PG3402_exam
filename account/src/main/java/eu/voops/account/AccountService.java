package eu.voops.account;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountService {

    private AccountRepository repository;

    public boolean checkIfAccountExistByPersonalId(String personalId) {
        return repository.existsByPersonalId(personalId);
    }
}
