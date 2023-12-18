package eu.voops.customer;

import eu.voops.customer.entity.Customer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository repository;

    private Customer customer;

    @BeforeEach
    public void beforeEach() {
        customer = new Customer("internalId1", "personalId1",
                "Ola", "Nordmann", "address",
                "123456789", "email@example.no");
    }

    @AfterEach
    public void afterEach() {
        entityManager.clear();
    }

    @Test
    void existsByPersonalId_userExists() {
        entityManager.persistAndFlush(customer);

        boolean exists = repository.existsByPersonalId(customer.getPersonalId());

        assertTrue(exists, "Expect to return true when a customer with the given personal ID exists.");
    }

    @Test
    void existsByPersonalId_userDoesNotExist() {
        String personalId = "123456789";

        boolean exists = repository.existsByPersonalId(personalId);

        assertFalse(exists, "Expect to return false when there is no customer with the given personal ID.");
    }

    @Test
    public void findByPersonalId_userExist() {
        entityManager.persistAndFlush(customer);

        Customer found = repository.findByPersonalId(customer.getPersonalId());

        assertNotNull(found);
        assertEquals(found.getPersonalId(), customer.getPersonalId());
    }

    @Test
    public void findByPersonalId_userDoesNotExist() {
        entityManager.persistAndFlush(customer);

        String fakeId = "fakeId";
        Customer found = repository.findByPersonalId(fakeId);

        assertNull(found);
    }

    @Test
    void existsByInternalId_userExists() {
        entityManager.persistAndFlush(customer);

        boolean exists = repository.existsByPersonalId(customer.getPersonalId());

        assertTrue(exists, "Expect to return true when a customer with the given internal ID exists.");
    }

    @Test
    void existsByInternalId_userDoesNotExist() {
        String internalId = "123456789";

        boolean exists = repository.existsByPersonalId(internalId);

        assertFalse(exists, "Expect to return false when there is no customer with the given internal ID.");
    }
    
    @Test
    void deleteByInternalId_userExists() {
        entityManager.persistAndFlush(customer);

        repository.deleteByInternalId(customer.getInternalId());

        assertFalse(repository.existsByInternalId(customer.getInternalId()), "Expect to return false when a customer with the given internal ID is deleted.");
    }
    
    @Test
    void deleteByInternalId_userDoesNotExist() {
        String internalId = "123456789";

        repository.deleteByInternalId(internalId);

        assertFalse(repository.existsByInternalId(internalId), "Expect to return false when there is no customer with the given internal ID.");
    }
    
}