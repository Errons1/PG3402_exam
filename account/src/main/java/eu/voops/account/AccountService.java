package eu.voops.account;

import eu.voops.account.exception.ProfileExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountService {

    private AccountRepository repository;

    public void createAccount(Account account) {
        if (repository.existsByInternalId(account.getInternalId())) {
            throw new ProfileExistException("Profile Exist");
        } else {
            repository.save(account);
        }
    }

    @Transactional
    public String makeAccountNumber() {
        Random random = new Random(System.currentTimeMillis());
        final int accountSize = 12;
        String account;

        do {
            account = random.ints(0, 10)
                    .mapToObj(Integer::toString)
                    .limit(accountSize)
                    .collect(Collectors.joining());
        } while (repository.existsByAccountNumber(account));

        return account;
    }

    @Transactional
    public void emergencyDelete(String internalId) {
        if (repository.existsByInternalId(internalId)) {
            repository.deleteByInternalId(internalId);
        } else {
            throw new NoSuchElementException("Profile does not exist");
        }
    }
}
