package eu.voops.authentication;

import eu.voops.authentication.dto.DtoCreateAccount;
import eu.voops.authentication.dto.DtoLogin;
import eu.voops.authentication.entity.Authentication;
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
public class AuthenticationController {

    private final AuthenticationService service;
    
    /**
     * Creates a new account by validating the input data, creating an authentication object and saving it.
     *
     * @param dao The data transfer object containing the internal ID, personal ID, and password of the user. Cannot be null.
     * @return A ResponseEntity object with a boolean indicating whether the account creation was successful.
     */
    @PostMapping("/create-authentication")
    public ResponseEntity<Boolean> createAuthenticationAccount(@Valid @RequestBody @NonNull DtoCreateAccount dao) {
        log.info("Controller: Attempting to make an account");
        Authentication authentication = new Authentication(dao.getInternalId(), dao.getPersonalId(), Hash.sha256(dao.getPassword()));
        service.createAccount(authentication);
        
        log.info("Controller: Successfully made an account");
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    /**
     * Deletes a profile in case of an emergency.
     *
     * @param internalId The internal ID of the profile to be deleted. Cannot be null or empty.
     * @return A ResponseEntity object indicating the success or failure of the delete operation.
     */
    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Try delete profile");
        service.emergencyDelete(internalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Logs in a user with the provided credentials.
     *
     * @param dto The data transfer object containing the user's personal ID and password. Cannot be null.
     * @return A ResponseEntity object with a boolean indicating whether the login was successful.
     */
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@Valid @RequestBody @NonNull DtoLogin dto) {
        log.info("Controller: Attempting to log in");
        boolean isLoggedIn = service.login(dto);
        
        if (isLoggedIn) {
            log.info("Controller: Successful login");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("Controller: Failed to login");
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }

}
