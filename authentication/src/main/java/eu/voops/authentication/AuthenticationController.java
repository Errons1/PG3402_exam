package eu.voops.authentication;

import eu.voops.authentication.dto.DtoCreateAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1")
@RestController()
public class AuthenticationController {

    private AuthenticationService service;
    
    @PostMapping("/create-authentication")
    public ResponseEntity<Boolean> createAccount(@Valid @RequestBody DtoCreateAccount dao) {
        Authentication authentication = new Authentication(dao.getInternalId(), dao.getPersonalId(), Hash.sha256(dao.getPassword()));
        
        log.info("Attempting to make an account");
        service.createAccount(authentication);
        
        log.info("Successfully made an account");
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @DeleteMapping("/emergency-delete/{internalId}")
    public ResponseEntity<Object> emergencyDelete(@PathVariable @NonNull String internalId) {
        log.info("Controller: Try delete profile");
        service.emergencyDelete(internalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
