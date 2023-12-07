package eu.voops.frontend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@Service
public class IndexService {
    
    RestTemplate restTemplate;
    
    public String demo() {
        String response = restTemplate.getForObject("http://demo1/api/v1/", String.class);
        log.info(response);  
        return response;
    }
}
