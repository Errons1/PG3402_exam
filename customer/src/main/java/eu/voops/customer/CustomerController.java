package eu.voops.customer;

import eu.voops.customer.dto.DtoCreateCustomer;
import eu.voops.customer.entity.Customer;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService service;

    /**
     * Checks if an account exists based on the provided personal ID.
     *
     * @param personalId The personal ID of the account to check.
     * @return ResponseEntity containing a Boolean value indicating if the account exists or not.
     */
    @GetMapping("/check-if-customer-exist/{personalId}")
    public ResponseEntity<Boolean> checkIfAccountExistByPersonalId(@PathVariable @NonNull String personalId) {
        log.info("Controller: Check if account exist by personal ID");
        boolean accountExist = service.checkIfAccountExistByPersonalId(personalId);

        log.info("Controller: Successfully checked existence of account");
        return new ResponseEntity<>(accountExist, HttpStatus.OK);
    }

    /**
     * Retrieves the internalId using the provided personalId.
     *
     * @param personalId the personalId used to retrieve the internalId
     * @return a ResponseEntity with the internalId if found, or HttpStatus.NOT_FOUND if not found
     */
    @GetMapping("/get-internal-id-by-personal-id/{personalId}")
    public ResponseEntity<String> getInternalIdByPersonalId(@PathVariable @NonNull String personalId) {
        log.info("Controller: Getting internal ID by Personal ID");

        String internalId = service.getInternalIdByPersonalId(personalId);
        if (internalId == null) {
            log.info("Controller: Internal ID not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("Controller: Intern ID found");
            return new ResponseEntity<>(internalId, HttpStatus.OK);
        }
    }

    /**
     * Creates a new customer based on the provided DTO.
     *
     * @param dto The DTO containing the customer details.
     * @return A ResponseEntity indicating the success of the customer creation.
     */
    @PostMapping("/create-customer")
    public ResponseEntity<Boolean> createCustomer(@Valid @RequestBody @NonNull DtoCreateCustomer dto) {
        log.info("Controller: Attempting to create customer");
        String internalId = service.createInternalId(dto);

        Customer customer = new Customer(
                internalId, dto.getPersonalId(), dto.getFirstName(),
                dto.getLastName(), dto.getAddress(), dto.getTlf(),
                dto.getEmail()
        );

        service.createCustomer(customer);
        log.info("Controller: Successfully created account");
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    /**
     * Deletes a profile in emergency mode using the specified internal ID.
     *
     * @param internalId the internal ID of the profile to be deleted
     * @return a ResponseEntity with HTTP status code 200 (OK) indicating successful deletion
     */
    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Attempting to delete customer");
        service.emergencyDelete(internalId);

        log.info("Controller: Successfully deleted customer");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves the full name for the given internal ID.
     *
     * @param internalId the internal ID of the user
     * @return a ResponseEntity instance with the full name as the body if found, otherwise returns a ResponseEntity with HTTP status code 404 (Not Found)
     * @since 1.0.0
     */
    @GetMapping("get-full-name/{internalId}")
    public ResponseEntity<String> getFullNameByInternalId(@PathVariable @NonNull String internalId) {
        log.info("Controller: Attempting to get full name by internal ID");
        String fullName = service.getFullName(internalId);
        
        if (fullName == null) {
            log.info("Controller: Full name not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            
        } else {
            log.info("Controller: Full name found");
            return new ResponseEntity<>(fullName, HttpStatus.OK);
        }
    }

}
