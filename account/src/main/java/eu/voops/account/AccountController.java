package eu.voops.account;

import eu.voops.account.dto.*;
import eu.voops.account.entity.Account;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService service;

    /**
     * Creates a new account with the given account details.
     *
     * @param dto The DTO containing the account details.
     * @return A ResponseEntity indicating the success or failure of the account creation operation.
     */
    @PostMapping("/create-account")
    public ResponseEntity<Boolean> createAccount(@Valid @RequestBody @NonNull DtoCreateAccount dto) {
        log.info("Controller: attempting to create account");
        String randomAccount = service.makeAccountNumber();
        final Long defaultAccountBalance = 1000L; 
        Account account = new Account(
                dto.getInternalId(), dto.getAccountName(), randomAccount, defaultAccountBalance
        );

        service.createAccount(account);
        log.info("Controller: successfully made account");
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    /**
     * Emergency delete a profile with the given internal ID.
     *
     * @param internalId The internal ID of the profile to delete.
     * @return A ResponseEntity indicating the success or failure of the delete operation.
     */
    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Attempting to delete accounts");
        service.emergencyDelete(internalId);
        
        log.info("Controller: Successfully deleted accounts");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Retrieves all accounts for a given internal ID.
     *
     * @param internalId The internal ID of the accounts to retrieve.
     * @return A ResponseEntity containing a list of DTO accounts.
     */
    @GetMapping("/get-all-accounts/{internalId}")
    public ResponseEntity<List<DtoAccount>> getAllAccounts(@PathVariable @NonNull String internalId) {
        log.info("Controller: Attempting to get all accounts");
        List<DtoAccount> accounts = service.getAllAccounts(internalId);
        
        log.info("Controller: Successfully got all accounts");
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    
    /**
     * Retrieves transfer data for the given DTO.
     *
     * @param dto The DTO containing the transfer details.
     * @return A ResponseEntity containing the transfer account balance.
     */
    @PostMapping("/get-transfer-data")
    public ResponseEntity<DtoTransferAccountBalance> getTransferData(@Valid @RequestBody @NonNull DtoTransfer dto) {
        log.info("Controller: Attempting to get transfer data");
        DtoTransferAccountBalance dtoTransferAccountBalance = service.getAccountBalance(dto);
        
        log.info("Controller: Successfully retrieved transfer data");
        return new ResponseEntity<>(dtoTransferAccountBalance, HttpStatus.OK);
    }
    
    /**
     * Updates the account balance for a transfer between two accounts.
     *
     * @param dto The DTO containing the new account balance details.
     * @return A ResponseEntity indicating the success or failure of the account balance update operation.
     */
    @PostMapping("/update-account-balance")
    public ResponseEntity<Boolean> updateAccountBalance(@Valid @RequestBody @NonNull DtoNewBalance dto) {
        log.info("Controller: Attempting to update account balance");
        service.updateAccounts(dto);
        
        log.info("Controller: Successfully updated account balance");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
