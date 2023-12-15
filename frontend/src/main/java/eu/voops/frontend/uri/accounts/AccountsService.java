package eu.voops.frontend.uri.accounts;

import eu.voops.frontend.dto.DtoAccount;
import lombok.AllArgsConstructor;
import org.instancio.Instancio;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@AllArgsConstructor
@Service
public class AccountsService {

    private RestTemplate restTemplate;

    public String getFullNameWithInternalId(String internalIdCookie) {
        return "Ola Nordmann";
    }

    public List<DtoAccount> getAllAccounts(String internalId) {
        List<DtoAccount> accounts = Instancio.createList(DtoAccount.class);

//        String url = "http://account/api/v1/get-all-accounts/" + internalId;
//        ResponseEntity<List<DtoAccount>> response = restTemplate.exchange(
//                url, HttpMethod.GET, null, new ParameterizedTypeReference<>(){}
//        );

//        return response.getBody();
        return accounts;
    }
}
