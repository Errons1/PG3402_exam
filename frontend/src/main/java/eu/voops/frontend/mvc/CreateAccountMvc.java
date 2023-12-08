package eu.voops.frontend.mvc;

import eu.voops.frontend.Account;
import eu.voops.frontend.dto.DtoCreateAccount;
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
        return "create-account/form";
    }

    @PostMapping("/")
    public String createAccountForm(@Valid @ModelAttribute DtoCreateAccount dto, Model model) {
        log.info("Controller: Attempting to create account");
        
        if (service.checkIfAccountExist(dto.getPersonalId())) {
            log.warn("Controller: Could not make account, account exist");
            throw new IllegalArgumentException("Account exist");
        }

        String internalId = service.createInternalId("dto");
        if (internalId.isBlank()) {
            log.warn("Controller: Could not make account, internal id was blank or null");
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
            
        } catch (Exception e) {
            log.warn("Controller: " + e.getMessage());
            model.addAttribute("isSuccessful", false);
            model.addAttribute("message", "Failed: " + e.getMessage());
            return "create-account/response";
        }

        log.info("Controller: Successfully made account");
        model.addAttribute("isSuccessful", true);
        model.addAttribute("message", "Successfully made account");
        return "create-account/response";
    }

}
