package eu.voops.frontend.uri.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.voops.frontend.dto.DtoAccount;
import eu.voops.frontend.dto.DtoTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransferService {
    
    @Value("${rabbit.exchange.transfer}") 
    private String exchangeName;
    
    @Value("${rabbit.key.transfer}") 
    private String key;
    
    private final RabbitTemplate rabbitTemplate;

    private final RestTemplate restTemplate;
    

    public List<DtoAccount> getAllAccounts(String internalId) {
        String url = "http://account/api/v1/get-all-accounts/" + internalId;
        ResponseEntity<DtoAccount[]> response = restTemplate.getForEntity(url, DtoAccount[].class);
        return List.of(response.getBody());
    }

    public String getFullNameWithInternalId(String internalId) {
        String url = "http://customer/api/v1/get-full-name/" + internalId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }

    public void transferMoney(DtoTransfer dto) throws JsonProcessingException {
//        String url = "http://account/api/v1/transfer";
//        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, dto, Boolean.class);
        String json = new ObjectMapper().writeValueAsString(dto);
        rabbitTemplate.convertAndSend(exchangeName, key, json);
        
    }
    
}
