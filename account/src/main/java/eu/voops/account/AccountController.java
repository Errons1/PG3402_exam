package eu.voops.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController()
@RequestMapping("/api/v1")
public class AccountController {

    private AccountService service;
    
}
