package eu.voops.account;

import eu.voops.account.exception.ProfileExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AccountServiceTests {

    @MockBean
    private AccountRepository repository;

    @Autowired
    private AccountService service;

    private Account account;

    @BeforeEach
    public void beforeEach() {
        account = new Account("internalId1", "account name", "123456789123", 0L);
    }
    
    @Test
    public void createAccount_successfully() {
        String internalId = account.getInternalId();
        when(repository.existsByInternalId(internalId)).thenReturn(false);
        
        service.createAccount(account);
        
        verify(repository).save(account);
    }
//    TODO: re-write this test later!
//    @Test
//    public void createAccount_failedToCreateAccount() {
//        String internalId = account.getInternalId();
//        when(repository.existsByInternalId(internalId)).thenReturn(true);
//
//        assertThrows(ProfileExistException.class, () -> service.createAccount(account), "CreateAccount should throw exception for existing internalId");
//
//        verify(repository, never()).save(account);
//    }
    
    @Test
    public void makeAccountNumber_successfully() {
        when(repository.existsByAccountNumber(anyString())).thenReturn(false);
        
        String accountNumber = service.makeAccountNumber();

        final int accountLength = 12;
        assertEquals(accountLength, accountNumber.length());
        
        verify(repository).existsByAccountNumber(anyString());
    }

    @Test
    public void emergencyDelete_successfullyDeletedProfile() {
        String internalId = account.getInternalId();
        when(repository.existsByInternalId(internalId)).thenReturn(true);
        doNothing().when(repository).deleteByInternalId(internalId);

        assertDoesNotThrow(() -> service.emergencyDelete(internalId), "EmergencyDelete should not throw any exception for valid internalId");

        verify(repository).existsByInternalId(internalId);
        verify(repository).deleteByInternalId(internalId);
    }

    @Test
    public void emergencyDelete_failedToDeletedProfile() {
        String internalId = account.getInternalId();
        when(repository.existsByInternalId(internalId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.emergencyDelete(internalId), "EmergencyDelete should throw exception for existing internalId");

        verify(repository).existsByInternalId(internalId);
    }

}
