package eu.voops.customer;

import eu.voops.customer.dto.DtoCreateCustomer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
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
    }


    @Test
    public void testCheckIfAccountExist_accountExist_status200() {
        repository.save(customer);
        String personalId = customer.getPersonalId();

        String url = "/api/v1/check-if-customer-exist/" + personalId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expects 200 OK from server");
        assertEquals(Boolean.TRUE, response.getBody(), "Expects TRUE that customer exist in the DB");
    }

    @Test
    public void testCheckIfAccountExist_accountDoesNotExist_status404() {
        String fakePersonalId = "fakeId";

        String url = "/api/v1/check-if-customer-exist/" + fakePersonalId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expects 404 NOT FOUND from server");
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

    @Test
    public void createCustomer_successfully_status201() {
        DtoCreateCustomer dto = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );

        String url = "/api/v1/create-customer";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, dto, Boolean.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Expects 201 Created from server");
        assertEquals(Boolean.TRUE, response.getBody(), "Expects TRUE that profile got made");
    }

    @Test
    public void createCustomer_accountExist_status409() {
        DtoCreateCustomer dto = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        repository.save(customer);

        String url = "/api/v1/create-customer";
        ResponseEntity<String> response = restTemplate.postForEntity(url, dto, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode(), "Expects 409 Created from server");
    }

    @Test
    public void createCustomer_wrongInput_status400() {
        DtoCreateCustomer dto1 = new DtoCreateCustomer(
                null, customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto2 = new DtoCreateCustomer(
                customer.getPersonalId(), null, customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto3 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), null,
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto4 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                null, customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto5 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), null, customer.getEmail()
        );
        DtoCreateCustomer dto6 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), null
        );

        String url = "/api/v1/create-customer";
        ResponseEntity<String> response1 = restTemplate.postForEntity(url, dto1, String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity(url, dto2, String.class);
        ResponseEntity<String> response3 = restTemplate.postForEntity(url, dto3, String.class);
        ResponseEntity<String> response4 = restTemplate.postForEntity(url, dto4, String.class);
        ResponseEntity<String> response5 = restTemplate.postForEntity(url, dto5, String.class);
        ResponseEntity<String> response6 = restTemplate.postForEntity(url, dto6, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode(), "Expects 400 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode(), "Expects 409 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode(), "Expects 409 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response4.getStatusCode(), "Expects 409 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response5.getStatusCode(), "Expects 409 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response6.getStatusCode(), "Expects 409 BAD REQUEST from server");
    }

    @Test
    public void emergencyDelete_successfully_status200() {
        String internalId = customer.getInternalId();
        repository.save(customer);

        String url = "/api/v1/emergency-delete/" + internalId;
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void emergencyDelete_accountDoesNotExist_status404() {
        String internalId = "fakeId";

        String url = "/api/v1/emergency-delete/" + internalId;
        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
