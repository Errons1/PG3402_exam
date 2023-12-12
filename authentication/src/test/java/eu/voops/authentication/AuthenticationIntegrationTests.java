package eu.voops.authentication;

import eu.voops.authentication.dto.DtoCreateAccount;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
    public void createAccount_validInput_status201() {
        DtoCreateAccount test = Instancio.create(DtoCreateAccount.class);
        String url = "/api/v1/create-account";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, dtoCreateAccount, Boolean.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Expects 201 CREATED from server");
        assertEquals(Boolean.TRUE, response.getBody(), "Expects TRUE that customer exist in the DB");
    }

    @Test
    public void createAccount_accountAlreadyExist() {
        repository.save(authentication);

        String url = "/api/v1/create-account";
        ResponseEntity<String> response = restTemplate.postForEntity(url, dtoCreateAccount, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expects 400 BAD REQUEST from server");
    }

    @Test
    public void createAccount_invalidInput() {
        String internalId = dtoCreateAccount.getInternalId();
        String personalId = dtoCreateAccount.getPersonalId();

        dtoCreateAccount.setInternalId("");
        String url = "/api/v1/create-account";
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


}
