package eu.voops.customer;

import eu.voops.customer.dto.DtoCreateCustomer;
import eu.voops.customer.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.NoSuchElementException;

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
    
    @Test
    public void createInternalId_successfullyMadeId() {
        DtoCreateCustomer dto = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        
        String internalId = "internalId1";
        when(repository.existsByInternalId(internalId)).thenReturn(false);
        
        String hash = service.createInternalId(dto);
    
        verify(repository).existsByInternalId(hash);
    }

    @Test
    public void createInternalId_failed() {
        DtoCreateCustomer dto = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );

        when(repository.existsByInternalId(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createInternalId(dto));
        
        verify(repository, times(5)).existsByInternalId(anyString());
    }
    
    @Test
    public void emergencyDelete_successfullyDeletedProfile() {
        String internalId = customer.getInternalId();
        when(repository.existsByInternalId(internalId)).thenReturn(true);
        doNothing().when(repository).deleteByInternalId(internalId);
        
        assertDoesNotThrow(() -> service.emergencyDelete(internalId), "EmergencyDelete should not throw any exception for valid internalId");

        verify(repository).existsByInternalId(internalId);
        verify(repository).deleteByInternalId(internalId);
    }

    @Test
    public void emergencyDelete_failedToDeletedProfile() {
        String internalId = customer.getInternalId();
        when(repository.existsByInternalId(internalId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.emergencyDelete(internalId), "EmergencyDelete should throw exception for existing internalId");

        verify(repository).existsByInternalId(internalId);
    }
}
