package eu.voops.account;

import eu.voops.account.dto.DtoAccount;
import eu.voops.account.dto.DtoNewBalance;
import eu.voops.account.dto.DtoTransfer;
import eu.voops.account.dto.DtoTransferAccountBalance;
import eu.voops.account.entity.Account;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository repository;

    public void createAccount(@NonNull Account account) {
        repository.save(account);
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

    public DtoTransferAccountBalance getAccountBalance(@NonNull DtoTransfer dto) {
        Account accountFrom = repository.findByAccountNumber(dto.getTransferFrom());
        Account accountTo = repository.findByAccountNumber(dto.getTransferTo());

        if (accountFrom != null && accountTo != null) {
            return new DtoTransferAccountBalance(accountFrom.getBalance(), accountTo.getBalance());
        } else {
            throw new NoSuchElementException("Account does not exist");
        }
    }

    @Transactional
    public void updateAccounts(@NonNull DtoNewBalance dto) {
        Account accountFrom = repository.findByAccountNumber(dto.getTransferFrom());
        Account accountTo = repository.findByAccountNumber(dto.getTransferTo());
        accountFrom.setBalance(dto.getTransferFromBalance());
        accountTo.setBalance(dto.getTransferToBalance());
        repository.save(accountFrom);
        repository.save(accountTo);
    }
}
