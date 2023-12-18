package eu.voops.account;

import eu.voops.account.dto.DtoAccount;
import eu.voops.account.dto.DtoCreateAccount;
import eu.voops.account.entity.Account;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository repository;

    private Account account;

    @BeforeEach
    public void beforeEach() {
        account = new Account("internalId1", "account name", "123456789123", 0L);
    }

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    }
    
    @Test
    public void testSaveAccount__successfully_status201() {
        DtoCreateAccount dto = new DtoCreateAccount(account.getInternalId(), account.getAccountName());
        
        String url = "/api/v1/create-account";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, dto, Boolean.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Expects 201 Created from server");
        assertEquals(Boolean.TRUE, response.getBody(), "Expects TRUE that profile got made");
    }

//    @Test
//    public void createAccount_accountExist_status409() {
//        DtoCreateAccount dto = new DtoCreateAccount(account.getInternalId(), account.getAccountName());
//        repository.save(account);
//
//        String url = "/api/v1/create-account";
//        ResponseEntity<String> response = restTemplate.postForEntity(url, dto, String.class);
//
//        assertEquals(HttpStatus.CONFLICT, response.getStatusCode(), "Expects 409 Created from server");
//    }

    @Test
    public void createCustomer_wrongInput_status400() {
        DtoCreateAccount dto1 = new DtoCreateAccount(null, account.getAccountName());
        DtoCreateAccount dto2 = new DtoCreateAccount(account.getInternalId(), null);

        String url = "/api/v1/create-account";
        ResponseEntity<String> response1 = restTemplate.postForEntity(url, dto1, String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity(url, dto2, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode(), "Expects 400 BAD REQUEST from server");
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode(), "Expects 409 BAD REQUEST from server");
    }

    @Test
    public void emergencyDelete_successfully_status200() {
        String internalId = account.getInternalId();
        repository.save(account);

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

    @Test
    public void getAllAccounts_accountsExist_status200() {
        String internalId = account.getInternalId();
        List<Account> accounts = new ArrayList<>();
        List<DtoAccount> dtoAccounts = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Account tmp = Instancio.create(Account.class);
            tmp.setInternalId(internalId);
            accounts.add(tmp);
            
            dtoAccounts.add(new DtoAccount(
                    tmp.getInternalId(), tmp.getAccountName(), 
                    tmp.getAccountNumber(), tmp.getBalance())
            );
        }
        repository.saveAll(accounts);
        repository.flush();
        
        String url = "/api/v1/get-all-accounts/" + internalId;
        ResponseEntity<DtoAccount[]> responseEntity = restTemplate.getForEntity(url, DtoAccount[].class);
        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertIterableEquals(dtoAccounts, Arrays.asList(responseEntity.getBody()));
    }

    @Test
    public void getAllAccounts_noAccountsExist_status200() {
        String url = "/api/v1/get-all-accounts/" + "internalId";
        ResponseEntity<DtoAccount[]> responseEntity = restTemplate.getForEntity(url, DtoAccount[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, Arrays.asList(responseEntity.getBody()).size());
    }

//    @Test
//    @Transactional
//    public void getTransferData_status200() throws Exception {
//        DtoTransfer dtoTransfer = Instancio.create(DtoTransfer.class);
//        Account account1 = Instancio.of(Account.class).ignore(field(Account::getId)).create();
//        Account account2 = Instancio.of(Account.class).ignore(field(Account::getId)).create();
//        
//        account1.setAccountNumber(dtoTransfer.getTransferFrom());
//        account2.setAccountNumber(dtoTransfer.getTransferTo());
//        repository.save(account1);
//        repository.save(account2);
//        repository.flush();
//        
//        
//        String url = "/api/v1/get-transfer-data";
//        ResponseEntity<DtoTransferAccountBalance> responseEntity = 
//                restTemplate.postForEntity(url, dtoTransfer, DtoTransferAccountBalance.class);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//        assertEquals(0, Arrays.asList(responseEntity.getBody()).size());
//    }
}
