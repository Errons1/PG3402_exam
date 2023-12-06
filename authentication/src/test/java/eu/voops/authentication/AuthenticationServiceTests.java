package eu.voops.authentication;

import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class AuthenticationServiceTests {

    @Autowired
    private AuthenticationService service;
    
    @Autowired
    private AuthenticationRepository repository;
    
    @BeforeEach
    public void beforeEach() {
    }
    
    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    } 
    
    @DisplayName("Valid input account creation")
    @Test
    public void validInputAccountCreationTest() {
        Authentication expectedAuth = Instancio.create(Authentication.class);
        service.createAccount(expectedAuth);
        
        Authentication actualAuth = repository.findByInternalIdLike(expectedAuth.getInternalId());
        
        Assertions.assertEquals(expectedAuth.getPersonalId(), actualAuth.getPersonalId());
        Assertions.assertEquals(expectedAuth.getInternalId(), actualAuth.getInternalId());
    }
    
    @DisplayName("Invalid input account creation")
    @Test
    public void invalidInputAccountCreation() throws NoSuchAlgorithmException {
        Authentication auth1 = new Authentication("", 123, new byte[] {10, 20});
        Authentication auth2 = new Authentication("asd123", -1, new byte[] {10, 20});
        Authentication auth3 = new Authentication("asd123", 123, null);
        
        Assertions.assertThrows(Exception.class, () -> service.createAccount(auth1));
        Assertions.assertThrows(Exception.class, () -> service.createAccount(auth2));
        Assertions.assertThrows(Exception.class, () -> service.createAccount(auth3));
    }
    
}
