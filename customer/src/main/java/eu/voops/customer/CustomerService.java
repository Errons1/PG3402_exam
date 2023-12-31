package eu.voops.customer;

import eu.voops.customer.dto.DtoCreateCustomer;
import eu.voops.customer.entity.Customer;
import eu.voops.customer.exception.ProfileExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository repository;

    public boolean checkIfAccountExistByPersonalId(String personalId) {
        return repository.existsByPersonalId(personalId);
    }

    public String getInternalIdByPersonalId(String personalId) {
        Customer customer = repository.findByPersonalId(personalId);
        if (customer == null) return null;
        else return customer.getInternalId();
    }

    public String createInternalId(DtoCreateCustomer dto) {
        String internalId;
        String hash;

        for (int i = 0; i < 5; i++) {
            internalId = (dto.getPersonalId() + dto.getFirstName() + dto.getLastName() + i);
            hash = String.valueOf(internalId.hashCode());

            if (!repository.existsByInternalId(hash)) {
                return hash;
            }
        }

        throw new IllegalArgumentException("Could not make internal ID");
    }

    @Transactional
    public void createCustomer(Customer customer) {
        if (repository.existsByPersonalId(customer.getPersonalId())) {
            throw new ProfileExistException();
        } else {
            repository.save(customer);
        }
    }

    @Transactional
    public void emergencyDelete(String internalId) {
        if (repository.existsByInternalId(internalId)) {
            repository.deleteByInternalId(internalId);
        } else{
            throw new NoSuchElementException("Profile does not exist");
        }
    }

    public String getFullName(String internalId) {
        StringBuilder builder = new StringBuilder();
        Customer customer = repository.findByInternalId(internalId);
        builder.append(customer.getFirstName());
        builder.append(" ");
        builder.append(customer.getLastName());
        return builder.toString();
    }
}
