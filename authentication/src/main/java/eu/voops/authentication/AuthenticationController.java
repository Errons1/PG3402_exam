package eu.voops.authentication;

import eu.voops.authentication.dto.DtoCreateAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1")
@RestController()
public class AuthenticationController {

    private AuthenticationService service;
    
    @PostMapping("/create-account")
    public ResponseEntity<Object> createAccount(@Valid @RequestBody DtoCreateAccount dao) throws NoSuchAlgorithmException {
        Authentication authentication = new Authentication(dao.getInternalId(), dao.getPersonalId(), Hash.sha256(dao.getPassword()));

        log.info("Attempting to make an account");
        service.createAccount(authentication);
        
        log.info("Successfully made an account");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
