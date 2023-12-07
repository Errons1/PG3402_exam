package eu.voops.frontend.mvc;

import eu.voops.frontend.Account;
import eu.voops.frontend.dto.DtoCreateAccount;
import eu.voops.frontend.exception.AccountExistException;
import eu.voops.frontend.service.CreateAccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/create-account")
public class CreateAccountMvc {

    CreateAccountService service;

    @GetMapping("/")
    public String createAccount() {
        return "create-account";
    }

    @PostMapping("/")
    public String createAccountForm(@Valid @ModelAttribute DtoCreateAccount dto, Model model) {
        log.info("Attempting to create account");
        String internalId = service.checkIfAccountExist(dto.getPersonalId());

        if (internalId.isBlank()) {
            throw new IllegalArgumentException("Internal ID was blank or null");
        }
        
        Account account = new Account(
                internalId, dto.getPersonalId(), dto.getFirstName(),
                dto.getLastName(), dto.getAddress(), dto.getTlf(),
                dto.getEmail(), dto.getPassword()
        );

        try {
            service.createAccountAtAuthentication(account);
            service.createAccountAtCustomer(account);
            service.createAccountAtAccount(account);
        } catch (AccountExistException e) {
            log.warn("Failed to create account");
            model.addAttribute("isSuccessful", false);
            model.addAttribute("message", "Failed to create account");
            return "create-account-response";
        }

        log.info("Successfully made account");
        model.addAttribute("isSuccessful", true);
        model.addAttribute("message", "Successfully made account");
        return "create-account-response";
    }

}
