package eu.voops.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    void deleteByInternalId(String internalId);

    boolean existsByInternalId(String internalId);

    List<Account> findAllByInternalId(String internalId);

    Account findByAccountNumber(String accountNumber);
}
