package eu.voops.frontend.uri.create_account;

import eu.voops.frontend.dto.DtoCustomer;
import eu.voops.frontend.dto.DtoCreateAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/create-account")
public class CreateAccountMvc {

    private final CreateAccountService service;

    /**
     * Retrieves the create account form.
     *
     * @return The view name for the create account form.
     */
    @GetMapping("")
    public String createAccount() {
        log.info("Controller: Serving creating/from");
        return "create-account/form";
    }

    /**
     * Creates an account form with the provided information.
     *
     * @param dto   The data transfer object containing the account information.
     *              Must not be null. Must be a valid and non-null object.
     * @param model The model to be populated with the result of the account creation.
     *              Must not be null.
     * @return The name of the view that will be rendered to display the result of the account creation.
     *         Returns "create-account/response" on a successful account creation.
     *         Returns the same view with error messages on a failed account creation.
     * @throws IllegalArgumentException If an account with the same personal ID already exists.
     */

    @PostMapping("")
    public String handleAccountCreation(@Valid @ModelAttribute @NonNull DtoCreateAccount dto, Model model) {
        log.info("Controller: Attempting to create account");
        DtoCustomer dtoCustomer = new DtoCustomer(
                "temp", dto.getPersonalId(), dto.getFirstName(),
                dto.getLastName(), dto.getAddress(), dto.getTlf(),
                dto.getEmail(), dto.getPassword()
        );

        try {
            if (service.checkIfAccountExist(dto.getPersonalId())) {
                log.warn("Controller: Failed to create account, account exist");
                throw new IllegalArgumentException("Account exist");
            }

            createProfile(dtoCustomer);

        } catch (Exception e) {
            log.warn("Controller: " + e.getMessage());
            
            try {
                log.info("Controller: Attempting emergency delete!");
                service.emergencyDelete(dtoCustomer);
            } catch (Exception e2) {
                log.error("Controller: " + e2.getMessage());
            }
            
            model.addAttribute("isSuccessful", false);
            model.addAttribute("message", "Failed: " + e.getMessage());
            log.info("Controller: Successfully executed emergency deletion");
            return "create-account/response";
        }

        model.addAttribute("isSuccessful", true);
        model.addAttribute("message", "Successfully made account");
        log.info("Controller: Successfully made account");
        return "create-account/response";
    }

    /**
     * Creates dummy data by creating a customer profile, authentication profile, and account profile for the customer.
     * If the customer's personal ID already exists in the system, a new customer object is created with a modified email address.
     * If any exception occurs during the process, the customer data is rolled back and an error message is returned.
     *
     * @param model The Model object used to pass data to the view.
     * @return The view name for the response.
     */
    @PostMapping("/create-dummy-data")
    public String createDummyData(Model model) {
        log.info("Controller: Attempting to create dummy data");
        DtoCustomer dtoCustomer = new DtoCustomer(
                "temp", "id", "Ola",
                "Nordmann", "address", "11223344",
                "ola@nordmann.no", "password"
        );

        try {
            if (service.checkIfAccountExist(dtoCustomer.getPersonalId())) {
                dtoCustomer = Instancio.create(DtoCustomer.class);
                dtoCustomer.setEmail("demo@demo.no");
            }

            createProfile(dtoCustomer);

        } catch (Exception e) {
            log.warn("Controller: " + e.getMessage());

            try {
                log.info("Controller: Attempting emergency delete!");
                service.emergencyDelete(dtoCustomer);
            } catch (Exception e2) {
                log.error("Controller: " + e2.getMessage());
            }

            model.addAttribute("isSuccessful", false);
            model.addAttribute("message", "Failed: " + e.getMessage());
            log.info("Controller: Successfully executed emergency deletion");
            return "create-account/response";
        }

        model.addAttribute("isSuccessful", true);
        String message = """
                Successfully made account!
                Username: %s
                Password: %s
                """.formatted(dtoCustomer.getPersonalId(), dtoCustomer.getPassword());
        model.addAttribute("message", message);
        log.info("Controller: Successfully made account");
        return "create-account/response";
    }

    /**
     * Creates a profile for a given customer.
     *
     * @param dtoCustomer the customer for whom to create the profile
     */
    private void createProfile(DtoCustomer dtoCustomer) {
        log.info("Controller: Attempt to make profile at Customer");
        service.createProfileAtCustomer(dtoCustomer);
        dtoCustomer.setInternalId(service.getInternalId(dtoCustomer.getPersonalId()));
        log.info("Controller: Made profile at Customer");

        log.info("Controller: Attempt to make profile at Authentication");
        service.createProfileAtAuthentication(dtoCustomer);
        log.info("Controller: Made profile at Authentication");

        log.info("Controller: Attempt to make profile at Account");
        service.createProfileAtAccount(dtoCustomer);
        log.info("Controller: Made profile at Account");
    }
    
}
