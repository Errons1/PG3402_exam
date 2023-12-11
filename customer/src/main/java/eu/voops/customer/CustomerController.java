package eu.voops.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController()
@RequestMapping("/api/v1")
public class CustomerController {

    private CustomerService service;

    /**
     * Checks if an account exists based on the provided personal ID.
     *
     * @param personalId the personal ID for which the existence of the account will be checked
     * @return Boolean if an account with the given personal ID exists, false otherwise
     */
    @GetMapping("/check-if-account-exist/{personalId}")
    public ResponseEntity<Boolean> checkIfAccountExistByPersonalId(@PathVariable String personalId) {
        log.info("Controller: check if account " + personalId + " exist");
        boolean accountExist = service.checkIfAccountExistByPersonalId(personalId);

        if (accountExist) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

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


}
