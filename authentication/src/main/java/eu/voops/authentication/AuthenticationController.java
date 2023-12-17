package eu.voops.authentication;

import eu.voops.authentication.dto.DtoCreateAccount;
import eu.voops.authentication.dto.DtoLogin;
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
public class AuthenticationController {

    private AuthenticationService service;
    
    /**
     * Creates a new account with the given information.
     *
     * @param dao The data transfer object containing the information needed to create the account.
     * @return A ResponseEntity with a Boolean indicating whether the account creation was successful.
     */
    @PostMapping("/create-authentication")
    public ResponseEntity<Boolean> createAccount(@Valid @RequestBody @NonNull DtoCreateAccount dao) {
        Authentication authentication = new Authentication(dao.getInternalId(), dao.getPersonalId(), Hash.sha256(dao.getPassword()));
        
        log.info("Attempting to make an account");
        service.createAccount(authentication);
        
        log.info("Successfully made an account");
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    /**
     * Deletes a profile in case of an emergency.
     *
     * @param internalId The internal ID of the profile to be deleted. Cannot be null.
     * @return A ResponseEntity object with an HTTP status code indicating the result of the deletion.
     */
    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Try delete profile");
        service.emergencyDelete(internalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Login method that validates the user credentials and logs them in.
     *
     * @param dto The data transfer object containing the personal ID and password of the user.
     * @return A ResponseEntity with a boolean indicating whether the login was successful.
     */
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@Valid @RequestBody DtoLogin dto) {
        log.info("Attempting to log in");
        boolean isLoggedIn = service.login(dto);
        
        if (isLoggedIn) {
            log.info("Successful login");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("Failed to login");
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }

}
