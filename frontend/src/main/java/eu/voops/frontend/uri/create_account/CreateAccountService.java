package eu.voops.frontend.uri.create_account;

import eu.voops.frontend.dto.DtoCustomer;
import eu.voops.frontend.dto.DtoCreateProfileAccount;
import eu.voops.frontend.dto.DtoCreateProfileAuthentication;
import eu.voops.frontend.dto.DtoCreateProfileCustomer;
import eu.voops.frontend.exception.AccountExistException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateAccountService {

    private final RestTemplate restTemplate;

    public String getInternalId(String personalId) {
        String url = "http://customer/api/v1/get-internal-id-by-personal-id/" + personalId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public boolean checkIfAccountExist(String personalId) {
        boolean accountExist;

        String url = "http://customer/api/v1/check-if-customer-exist/" + personalId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        accountExist = Boolean.TRUE.equals(response.getBody());

        return accountExist;
    }

    public void createProfileAtCustomer(@NonNull DtoCustomer dtoCustomer) {
        DtoCreateProfileCustomer dto = new DtoCreateProfileCustomer(
                dtoCustomer.getPersonalId(), dtoCustomer.getFirstName(), dtoCustomer.getLastName(),
                dtoCustomer.getAddress(), dtoCustomer.getTlf(), dtoCustomer.getEmail()
        );
        
        String url = "http://customer/api/v1/create-customer";
        restTemplate.postForEntity(url, dto, Boolean.class);
    }



    public void createProfileAtAuthentication(@NonNull DtoCustomer dtoCustomer) {
        DtoCreateProfileAuthentication dto = new DtoCreateProfileAuthentication(
                dtoCustomer.getInternalId(), dtoCustomer.getPersonalId(), dtoCustomer.getPassword()
        );

        try {
            restTemplate.postForEntity("http://authentication/api/v1/create-authentication", dto, String.class);

        } catch (HttpClientErrorException e) {
            log.warn("Service: Error creating account at Authentication");
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) throw new AccountExistException("Account already exist");
            else throw new RestClientException(e.getMessage());
        }
    }


    public void createProfileAtAccount(DtoCustomer dtoCustomer) {
        DtoCreateProfileAccount dto = new DtoCreateProfileAccount(
                dtoCustomer.getInternalId(), "Main Account"
        );
        
        String url = "http://account/api/v1/create-account";
        restTemplate.postForEntity(url, dto, Boolean.class);
    }

    public void emergencyDelete(DtoCustomer dtoCustomer) {
        String urlCustomer = "http://customer/api/v1/emergency-delete/" + dtoCustomer.getInternalId();
        String urlAuthentication = "http://authentication/api/v1/emergency-delete/" + dtoCustomer.getInternalId();
        String urlAccount = "http://account/api/v1/emergency-delete/" + dtoCustomer.getInternalId();
        
        restTemplate.delete(urlCustomer);
        restTemplate.delete(urlAuthentication);
        restTemplate.delete(urlAccount);
    }
}
