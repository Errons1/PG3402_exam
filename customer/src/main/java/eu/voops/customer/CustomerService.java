package eu.voops.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerService {
    
    CustomerRepository repository;

    public boolean checkIfAccountExistByPersonalId(String personalId) {
        log.info(personalId);  
        return repository.existsByPersonalId(personalId);
    }

    public String getInternalIdByPersonalId(String personalId) {
        Customer customer = repository.findByPersonalId(personalId);
        if (customer == null) return null;
        else return customer.getInternalId();
    }
    
}
