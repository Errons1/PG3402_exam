package eu.voops.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository repository;

    private Customer customer;

    @BeforeEach
    public void beforeEach() {
        customer = new Customer("internalId1", "personalId1", "Ola", "Nordmann", "address", "123456789", "email@example.no");
    }

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
        customer = null;
    }

    
    @Test
    public void testCheckIfAccountExist_accountExist_status200() {
        repository.save(customer);
        String personalId = customer.getPersonalId();
        
        String url = "/api/v1/check-if-account-exist/" + personalId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expects 200 OK from server");
        assertEquals(Boolean.TRUE, response.getBody(), "Expects TRUE that customer exist in the DB");
    }

    @Test
    public void testCheckIfAccountExist_accountDoesNotExist_status404() {
        String fakePersonalId = "fakeId";
        
        String url = "/api/v1/check-if-account-exist/" + fakePersonalId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expects 404 NOT FOUND from server");
        assertEquals(Boolean.FALSE, response.getBody(), "Expects False that customer does not exist in the DB");
    }

    @Test
    public void getInternalIdByPersonalId_idFound_status200() {
        String personalId = customer.getPersonalId();
        String internalId = customer.getInternalId();
        repository.save(customer);
        
        String url = "/api/v1/get-internal-id-by-personal-id/" + personalId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expects 200 OK from server");
        assertEquals(internalId, response.getBody(), "Expects TRUE that customer exist in the DB");
    }

    @Test
    public void getInternalIdByPersonalId_idNotFound_status404() {
        String personalId = "testPersonalId";
        
        String url = "/api/v1/get-internal-id-by-personal-id/" + personalId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expects 404 NOT FOUND from server");
        assertNull(response.getBody(), "Expects False that customer does not exist in the DB");
    }

}
