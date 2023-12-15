package eu.voops.account;

import eu.voops.account.dto.DtoAccount;
import eu.voops.account.dto.DtoCreateAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private AccountService service;

    /**
     * Creates a new account with the provided account details.
     *
     * @param dto The DTO containing the account details.
     * @return A ResponseEntity indicating the success or failure of the account creation.
     */
    @PostMapping("/create-account")
    public ResponseEntity<Boolean> createAccount(@Valid @RequestBody @NonNull DtoCreateAccount dto) {
        log.info("Controller: attempting to create account");
        String randomAccount = service.makeAccountNumber();
        Account account = new Account(
                dto.getInternalId(), dto.getAccountName(), randomAccount, 0L
        );

        service.createAccount(account);
        log.info("Controller: successfully made account");
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    /**
     * Deletes a profile in an emergency situation.
     *
     * @param internalId The internal ID of the profile to be deleted.
     * @return A ResponseEntity indicating the success or failure of the deletion operation.
     */
    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Try delete profile");
        service.emergencyDelete(internalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Retrieves all accounts for a given internal ID.
     *
     * @param internalId The internal ID of the accounts to retrieve.
     * @return A ResponseEntity containing a list of Account objects indicating the success or failure of the retrieval operation.
     */
    @GetMapping("/get-all-accounts/{internalId}")
    public ResponseEntity<List<DtoAccount>> getAllAccounts(@PathVariable @NonNull String internalId) {
        log.info("Controller: Getting all accounts");
        List<DtoAccount> accounts = service.getAllAccounts(internalId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

}
