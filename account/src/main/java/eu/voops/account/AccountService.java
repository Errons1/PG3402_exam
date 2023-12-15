package eu.voops.account;

import eu.voops.account.dto.DtoAccount;
import eu.voops.account.exception.ProfileExistException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<DtoAccount> getAllAccounts(String internalId) {
        List<Account> accounts = repository.findAllByInternalId(internalId);
        List<DtoAccount> dtoAccounts = new ArrayList<>(accounts.size());
        
        for (Account account : accounts) {
            DtoAccount dto = new DtoAccount(
                    account.getInternalId(), account.getAccountName(),
                    account.getAccountNumber(), account.getBalance()
            );    
            
            dtoAccounts.add(dto);
        }
        
        return dtoAccounts;
    }
}
