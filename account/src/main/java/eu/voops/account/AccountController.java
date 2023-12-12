package eu.voops.account;

import eu.voops.account.dto.DtoCreateAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private AccountService service;

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

    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Try delete profile");
        service.emergencyDelete(internalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
