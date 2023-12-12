package eu.voops.customer;

import eu.voops.customer.dto.DtoCreateCustomer;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController()
@RequestMapping("/api/v1")
public class CustomerController {

    private CustomerService service;

    /**
     * Creates a customer based on the provided DTO.
     *
     * @param dto the DTO containing the customer information
     * @return Boolean indicating the success of creating the customer
     */
    @PostMapping("/create-customer")
    public ResponseEntity<Boolean> createCustomer(@Valid @RequestBody DtoCreateCustomer dto) {
        String internalId = service.createInternalId(dto);

        Customer customer = new Customer(
                internalId, dto.getPersonalId(), dto.getFirstName(),
                dto.getLastName(), dto.getAddress(), dto.getTlf(),
                dto.getEmail()
        );

        service.createCustomer(customer);
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    /**
     * Checks if an account exists based on the provided personal ID.
     *
     * @param personalId the personal ID for which the existence of the account will be checked
     * @return Boolean if an account with the given personal ID exists, false otherwise
     */
    @GetMapping("/check-if-customer-exist/{personalId}")
    public ResponseEntity<Boolean> checkIfAccountExistByPersonalId(@PathVariable String personalId) {
        log.info("Controller: check if account " + personalId + " exist");
        boolean accountExist = service.checkIfAccountExistByPersonalId(personalId);
        return new ResponseEntity<>(accountExist, HttpStatus.OK);

    }

    /**
     * Retrieves the internal ID of a customer based on their personal ID.
     *
     * @param personalId the personal ID of the customer
     * @return the internal ID of the customer
     */
    @GetMapping("/get-internal-id-by-personal-id/{personalId}")
    public ResponseEntity<String> getInternalIdByPersonalId(@PathVariable String personalId) {
        log.info("Controller: Getting internalId by " + personalId);
        String internalId = service.getInternalIdByPersonalId(personalId);
        if (internalId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(internalId, HttpStatus.OK);
        }
    }
    
    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Try delete profile");
        service.emergencyDelete(internalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
