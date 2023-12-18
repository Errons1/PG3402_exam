package eu.voops.authentication;

import eu.voops.authentication.dto.DtoCreateAccount;
import eu.voops.authentication.entity.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthenticationRepository repository;

    private Authentication authentication;

    private DtoCreateAccount dtoCreateAccount;

    @BeforeEach
    public void beforeEach() {
        String internalId = "internalId1";
        String personalId = "personalId1";
        String password = "password";
        dtoCreateAccount = new DtoCreateAccount(internalId, personalId, password);
        authentication = new Authentication(internalId, personalId, Hash.sha256(password));
    }

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    }

    @Test
    public void createAuthentication_validInput_status201() {
        String url = "/api/v1/create-authentication";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, dtoCreateAccount, Boolean.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Expects 201 CREATED from server");
        assertEquals(Boolean.TRUE, response.getBody(), "Expects TRUE that customer exist in the DB");
    }

    @Test
    public void createAuthentication_accountAlreadyExist() {
        repository.save(authentication);

        String url = "/api/v1/create-authentication";
        ResponseEntity<String> response = restTemplate.postForEntity(url, dtoCreateAccount, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expects 400 BAD REQUEST from server");
    }

    @Test
    public void createAuthentication_invalidInput() {
        String internalId = dtoCreateAccount.getInternalId();
        String personalId = dtoCreateAccount.getPersonalId();

        dtoCreateAccount.setInternalId("");
        String url = "/api/v1/create-authentication";
        ResponseEntity<String> response1 = restTemplate.postForEntity(url, dtoCreateAccount, String.class);

        dtoCreateAccount.setInternalId(internalId);
        dtoCreateAccount.setPersonalId("");
        ResponseEntity<String> response2 = restTemplate.postForEntity(url, dtoCreateAccount, String.class);

        dtoCreateAccount.setPersonalId(personalId);
        dtoCreateAccount.setPassword("");
        ResponseEntity<String> response3 = restTemplate.postForEntity(url, dtoCreateAccount, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode(), "Expects 400 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode(), "Expects 400 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode(), "Expects 400 BAD REQUEST from server");
    }

    @Test
    public void emergencyDelete_successfully_status200() {
        String internalId = authentication.getInternalId();
        repository.save(authentication);

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
