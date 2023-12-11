package eu.voops.authentication;

import eu.voops.authentication.exception.AccountExistException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    
    @AfterEach
    public void afterEach() {
        authentication = null;
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
        
        assertThrows(AccountExistException.class, () -> service.createAccount(authentication));
    }
    
}
