package eu.voops.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerServiceTest {
    
    @MockBean
    private CustomerRepository repository;
    
    @Autowired
    private CustomerService service;
    
    private Customer customer;

    @BeforeEach
    public void beforeEach() {
        customer = new Customer("internalId1", "personalId1",
                "Ola", "Nordmann", "address",
                "123456789", "email@example.no");
    }

    @AfterEach
    public void afterEach() {
        customer = null;
    }

    @Test
    public void checkIfAccountExistByPersonalId_accountExist() {
        String testPersonalId = "12345678";

        when(repository.existsByPersonalId(anyString())).thenReturn(true);

        boolean exists = service.checkIfAccountExistByPersonalId(testPersonalId);

        assertTrue(exists);
        verify(repository).existsByPersonalId(eq(testPersonalId));
    }

    @Test
    public void checkIfAccountExistByPersonalId_accountDoesNotExists() {
        String testPersonalId = "12345679";

        when(repository.existsByPersonalId(anyString())).thenReturn(false);

        boolean exists = service.checkIfAccountExistByPersonalId(testPersonalId);

        assertFalse(exists);
        verify(repository).existsByPersonalId(eq(testPersonalId));
    }

    @Test
    public void getInternalIdByPersonalId_accountExist() {
        String personalId = customer.getPersonalId();
        String internalId = customer.getInternalId();
        
        when(repository.findByPersonalId(personalId)).thenReturn(customer);

        String result = service.getInternalIdByPersonalId(personalId);
        assertEquals(internalId, result);
    }

    @Test
    public void getInternalIdByPersonalId_accountDoesNotExist() {
        String personalId = customer.getPersonalId();

        when(repository.findByPersonalId(personalId)).thenReturn(null);

        String result = service.getInternalIdByPersonalId(personalId);
        assertNull(result);
    }
}
