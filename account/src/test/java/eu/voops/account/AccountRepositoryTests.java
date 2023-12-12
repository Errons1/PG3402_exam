package eu.voops.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AccountRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository repository;

    private Account account;

    @BeforeEach
    public void beforeEach() {
        account = new Account("internalId1", "account name", "123456789123", 0L);
    }

    @AfterEach
    public void afterEach() {
        entityManager.clear();
    }

    @Test
    void existsByAccountNumber_userExists() {
        entityManager.persistAndFlush(account);

        boolean exists = repository.existsByAccountNumber(account.getAccountNumber());

        assertTrue(exists, "Expect to return true when a customer with the given internal ID exists.");
    }

    @Test
    void existsByAccountNumber_userDoesNotExist() {
        String internalId = "123456789";

        boolean exists = repository.existsByAccountNumber(internalId);

        assertFalse(exists, "Expect to return false when there is no customer with the given internal ID.");
    }

    @Test
    void deleteByInternalId_userExists() {
        entityManager.persistAndFlush(account);

        repository.deleteByInternalId(account.getInternalId());

        assertFalse(repository.existsByInternalId(account.getInternalId()), "Expect to return false when a customer with the given internal ID is deleted.");
    }

    @Test
    void deleteByInternalId_userDoesNotExist() {
        String internalId = "123456789";

        repository.deleteByInternalId(internalId);

        assertFalse(repository.existsByInternalId(internalId), "Expect to return false when there is no customer with the given internal ID.");
    }

    @Test
    void existsByInternalId_userExists() {
        entityManager.persistAndFlush(account);

        boolean exists = repository.existsByInternalId(account.getInternalId());

        assertTrue(exists, "Expect to return true when a customer with the given internal ID exists.");
    }

    @Test
    void existsByInternalId_userDoesNotExist() {
        String internalId = "123456789";

        boolean exists = repository.existsByInternalId(internalId);

        assertFalse(exists, "Expect to return false when there is no customer with the given internal ID.");
    }

}
