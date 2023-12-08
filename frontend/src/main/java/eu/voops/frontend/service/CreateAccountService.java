package eu.voops.frontend.service;

import eu.voops.frontend.Account;
import eu.voops.frontend.dto.DtoCreateAccountAuthentication;
import eu.voops.frontend.exception.AccountExistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@Service
public class CreateAccountService {

    RestTemplate restTemplate;
    
    public boolean checkIfAccountExist(String personalId) {
        return false;
    }
    
    public String createInternalId(String account) {
        return "abc123";
    }

    public void createAccountAtAuthentication(Account account) {
        DtoCreateAccountAuthentication dto = new DtoCreateAccountAuthentication(account.getInternalId(), account.getPersonalId(), account.getPassword());
        
        try {
            restTemplate.postForEntity("http://authentication/api/v1/create-account", dto, String.class);
        
        } catch (HttpClientErrorException e) {
            log.warn("Service: Error creating account at Authentication");
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) throw new AccountExistException("Account already exist");
            else throw new RestClientException(e.getMessage());            
        }
    }

    public void createAccountAtCustomer(Account account) {
        
    }

    public void createAccountAtAccount(Account account) {

    }
    
}
