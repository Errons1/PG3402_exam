package eu.voops.frontend.uri.accounts;

import eu.voops.frontend.dto.DtoAccount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/accounts")
public class AccountsMvc {
    
    private AccountsService service;
    
    @GetMapping("")
    public String accounts(Model model,
                           @CookieValue(value = "internalId", required = false) String  internalIdCookie,
                           @CookieValue(value = "isLoggedIn", required = false) String isLoggedInCookie
    ) {
        model.addAttribute("isLoggedIn", isLoggedInCookie);
        model.addAttribute("internalId", internalIdCookie);
        
        String user = service.getFullNameWithInternalId(internalIdCookie);
        model.addAttribute("user", user);
        
        return "accounts/accounts";
    }
    
    @GetMapping("/get-all-accounts")
    public String getAllAccounts(Model model,
                           @CookieValue(value = "internalId", required = false) String  internalIdCookie,
                           @CookieValue(value = "isLoggedIn", required = false) String isLoggedInCookie
    ) {
        log.info("getting all account information");
        List<DtoAccount> accounts = service.getAllAccounts(internalIdCookie);
        model.addAttribute("accounts", accounts);

        return "fragments/element-accounts";
    }
}
