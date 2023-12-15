package eu.voops.frontend.uri.accounts;

import eu.voops.frontend.dto.DtoAccount;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@AllArgsConstructor
@Service
public class AccountsService {

    private RestTemplate restTemplate;

    public String getFullNameWithInternalId(String internalId) {
        String url = "http://customer/api/v1/get-full-name/" + internalId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        return response.getBody();
    }

    public List<DtoAccount> getAllAccounts(String internalId) {
        String url = "http://account/api/v1/get-all-accounts/" + internalId;
        ResponseEntity<DtoAccount[]> response = restTemplate.getForEntity(url, DtoAccount[].class);
        return List.of(response.getBody());
    }
}
