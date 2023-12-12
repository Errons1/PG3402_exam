package eu.voops.account;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountService {

    private AccountRepository repository;

    public void createAccount(Account account) {
        repository.save(account);
    }

    public String makeAccountNumber() {
        Random random = new Random(System.currentTimeMillis());
        String account;
        final int accountSize = 12;

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
        repository.deleteByInternalId(internalId);
    }
}
