package eu.voops.authentication;

import eu.voops.authentication.entity.Authentication;
import eu.voops.authentication.exception.ProfileExistException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceTests {

    @Autowired
    private AuthenticationService service;
    
    @MockBean
    private AuthenticationRepository repository;
    
    private Authentication authentication;
    
    @BeforeEach
    public void beforeEach() {
        byte[] hash = Hash.sha256("password");
        authentication = new Authentication("internalId1", "personalId1", hash);
    }
    
    @Test
    public void createAccount_validInput() {
        when(repository.existsByInternalId(authentication.getInternalId())).thenReturn(false);
        
        service.createAccount(authentication);
        
        verify(repository).save(authentication);
    }

    @Test
    public void createAccount_authAlreadyExist() {
        when(repository.existsByInternalId(authentication.getInternalId())).thenReturn(true);
        
        assertThrows(ProfileExistException.class, () -> service.createAccount(authentication));
    }

    @Test
    public void emergencyDelete_successfullyDeletedProfile() {
        String internalId = authentication.getInternalId();
        when(repository.existsByInternalId(internalId)).thenReturn(true);
        doNothing().when(repository).deleteByInternalId(internalId);

        assertDoesNotThrow(() -> service.emergencyDelete(internalId), "EmergencyDelete should not throw any exception for valid internalId");

        verify(repository).existsByInternalId(internalId);
        verify(repository).deleteByInternalId(internalId);
    }

    @Test
    public void emergencyDelete_failedToDeletedProfile() {
        String internalId = authentication.getInternalId();
        when(repository.existsByInternalId(internalId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.emergencyDelete(internalId), "EmergencyDelete should throw exception for existing internalId");

        verify(repository).existsByInternalId(internalId);
    }
    
}
