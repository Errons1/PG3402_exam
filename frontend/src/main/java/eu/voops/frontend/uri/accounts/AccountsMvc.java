package eu.voops.frontend.uri.accounts;

import eu.voops.frontend.dto.DtoAccount;
import eu.voops.frontend.dto.DtoCreateProfileAccount;
import eu.voops.frontend.dto.DtoNewAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/accounts")
public class AccountsMvc {

    private final AccountsService service;

    /**
     * This method handles the "/accounts" GET request and returns the "accounts/accounts" view.
     *
     * @param model            the model used to pass data to the view
     * @param internalIdCookie the value of the "internalId" cookie, can be null
     * @param isLoggedInCookie the value of the "isLoggedIn" cookie, can be null
     * @return the name of the view to render
     */
    @GetMapping("")
    public String accounts(@NonNull Model model,
                           @CookieValue(value = "internalId", required = false) String internalIdCookie,
                           @CookieValue(value = "isLoggedIn", required = false) String isLoggedInCookie
    ) {
        log.info("Controller: Attempting to server /accounts");
        model.addAttribute("isLoggedIn", isLoggedInCookie);
        model.addAttribute("internalId", internalIdCookie);

        String user = service.getFullNameWithInternalId(internalIdCookie);
        model.addAttribute("user", user);

        log.info("Controller: Serving /accounts");
        return "accounts/accounts";
    }

    /**
     * This method retrieves all the accounts information and adds it to the model for rendering.
     *
     * @param model            the model used to pass data to the view
     * @param internalIdCookie the value of the "internalId" cookie, can be null
     * @return the name of the view to render
     */
    @GetMapping("/get-all-accounts")
    public String getAllAccounts(@NonNull Model model,
                                 @CookieValue(value = "internalId", required = false) String internalIdCookie
    ) {
        log.info("Controller: Attempting to get all account info");
        List<DtoAccount> accounts = service.getAllAccounts(internalIdCookie);
        model.addAttribute("accounts", accounts);

        log.info("Controller: Successfully serving all accounts info");
        return "fragments/element-accounts";
    }

    /**
     * This method returns the name of the view to render for the "/new-account" GET request.
     *
     * @return the name of the view to render
     */
    @GetMapping("/new-account")
    public String newAccountPage() {
        log.info("Controller: Serving account/new-account");
        return "accounts/new-account";
    }

    /**
     * This method handles the "/new-account" POST request and creates a new account based on the provided data.
     *
     * @param model            the model used to pass data to the view
     * @param dto              the data transfer object containing the account details
     * @param internalIdCookie the value of the "internalId" cookie, can be null
     * @return the name of the view to render
     */
    @PostMapping("/new-account")
    public String createNewAccount(@NonNull Model model,
                                   @Valid @ModelAttribute @NonNull DtoNewAccount dto,
                                   @CookieValue(value = "internalId", required = false) String internalIdCookie
    ) {
        log.info("Controller: Attempting to create new account");
        DtoCreateProfileAccount newAccount = new DtoCreateProfileAccount(
                internalIdCookie, dto.getAccountName()
        );

        service.createNewAccount(newAccount);
        String user = service.getFullNameWithInternalId(internalIdCookie);
        model.addAttribute("user", user);

        log.info("Controller: Successfully created new account");
        return "accounts/accounts";
    }
}
