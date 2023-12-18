package eu.voops.frontend.uri.login;

import eu.voops.frontend.dto.DtoLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class LoginService {
    
    private final RestTemplate restTemplate;
    
    public boolean login(DtoLogin dto) {
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity("http://authentication/api/v1/login", dto, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
            
        } catch (HttpClientErrorException e) {
            if ( e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return false;
            }
        }
        
        return false;
    }

    public String getInternalId(String personalId) {
        String url = "http://customer/api/v1/get-internal-id-by-personal-id/" + personalId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
    
}
