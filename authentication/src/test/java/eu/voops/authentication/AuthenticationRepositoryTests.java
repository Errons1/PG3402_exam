package eu.voops.authentication;

import eu.voops.authentication.entity.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AuthenticationRepositoryTests {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private AuthenticationRepository repository;
    
    private Authentication authentication;
    
    @BeforeEach
    public void beforeEach() {
        byte[] hash = Hash.sha256("password");
        authentication = new Authentication("internalId1", "personalId1", hash);
    }
    
    @AfterEach
    public void afterEach() {
        entityManager.clear();
    }
    
    @Test
    public void existsByInternalId_authExist() {
        entityManager.persistAndFlush(authentication);
        
        boolean exists = repository.existsByInternalId(authentication.getInternalId());

        assertTrue(exists, "Should return TRUE if user exist");
    }
    
    @Test
    public void  existsByInternalId_authDoesNotExist() {
        boolean exists = repository.existsByInternalId(authentication.getInternalId());
        
        assertFalse(exists, "Should return TRUE if user exist");
    }
    
    @Test
    public void findByInternalIdLike_authExist() {
        entityManager.persistAndFlush(authentication);
    
        Authentication foundAuthentication = repository.findByInternalIdLike(authentication.getInternalId());
    
        assertNotNull(foundAuthentication, "Should return the authentication if it exist");
        assertEquals(authentication, foundAuthentication, "Should be equal if auth exist");
    }
    
    @Test
    public void findByInternalIdLike_authDoesNotExist() {
        Authentication notFoundAuthentication = repository.findByInternalIdLike(authentication.getInternalId());

        assertNull(notFoundAuthentication, "Should return NULL if auth does not exist");
    }

    @Test
    void deleteByInternalId_userExists() {
        entityManager.persistAndFlush(authentication);

        repository.deleteByInternalId(authentication.getInternalId());

        assertFalse(repository.existsByInternalId(authentication.getInternalId()), "Expect to return false when a customer with the given internal ID is deleted.");
    }

    @Test
    void deleteByInternalId_userDoesNotExist() {
        String internalId = "123456789";

        repository.deleteByInternalId(internalId);

        assertFalse(repository.existsByInternalId(internalId), "Expect to return false when there is no customer with the given internal ID.");
    }
    
}
