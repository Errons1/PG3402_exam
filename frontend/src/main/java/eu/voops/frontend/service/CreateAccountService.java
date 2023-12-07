package eu.voops.frontend.service;

import eu.voops.frontend.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class CreateAccountService {

    RestTemplate restTemplate;
    
    public String checkIfAccountExist(String personalId) {
        return "123abc";
    }

    public void createAccountAtAuthentication(Account account) {
        
    }

    public void createAccountAtCustomer(Account account) {
        
    }

    public void createAccountAtAccount(Account account) {

    }
    
}
