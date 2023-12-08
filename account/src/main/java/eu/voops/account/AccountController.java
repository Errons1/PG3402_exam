package eu.voops.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController()
@RequestMapping("/api/v1")
public class AccountController {

    private AccountService service;
    
    /** 
     * Check if account exist in DB
     * @param personalId string of personal ID of person
     * @return boolean */
    @GetMapping("/check-if-account-exist/{personalId}")
    public ResponseEntity<Boolean> checkIfAccountExist(@PathVariable String personalId) {
        log.info("Controller: check if account " + personalId + " exist");

        if (personalId.isBlank()) {
            log.warn("Controller: Invalid ID formatting");
            throw new IllegalArgumentException("Invalid ID formatting");
        }

        boolean accountExist = service.checkIfAccountExistByPersonalId(personalId);
        return new ResponseEntity<>(accountExist, HttpStatus.OK);
    }

}
