package eu.voops.frontend.uri.accounts;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/accounts")
public class AccountsMvc {
    
    @GetMapping("")
    public String accounts(Model model,
                           @CookieValue(value = "internalId", required = false) String  internalIdCookie,
                           @CookieValue(value = "isLoggedIn", required = false) String isLoggedInCookie
    ) {
        model.addAttribute("isLoggedIn", isLoggedInCookie);
        model.addAttribute("internalId", internalIdCookie);
        model.addAttribute("user", "User: Test");
        return "accounts/accounts";
    }
    
}
