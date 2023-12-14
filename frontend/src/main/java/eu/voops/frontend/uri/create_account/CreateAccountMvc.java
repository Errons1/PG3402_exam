package eu.voops.frontend.uri.create_account;

import eu.voops.frontend.Customer;
import eu.voops.frontend.dto.DtoCreateAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/create-account")
public class CreateAccountMvc {

    CreateAccountService service;

    /**
     * This method is used to get the create account form.
     *
     * @return The name of the HTML form file.
     */
    @GetMapping("")
    public String createAccount() {
        return "create-account/form";
    }

    /**
     * This method is used to submit the create account form and attempt to create a new account.
     *
     * @param dto   The data transfer object containing the account information.
     * @param model The model used to add attributes for the response view.
     * @return The name of the response view file.
     */

    @PostMapping("")
    public String createAccountForm(@Valid @NonNull @ModelAttribute DtoCreateAccount dto, Model model) {
        log.info("Controller: Attempting to create account");
        Customer customer = new Customer(
                "temp", dto.getPersonalId(), dto.getFirstName(),
                dto.getLastName(), dto.getAddress(), dto.getTlf(),
                dto.getEmail(), dto.getPassword()
        );

        try {
            if (service.checkIfAccountExist(dto.getPersonalId())) {
                throw new IllegalArgumentException("Account exist");
            }

            service.createProfileAtCustomer(customer);
            customer.setInternalId(service.getInternalId(customer.getPersonalId()));
            log.info("Controller: Made profile at Customer");
            service.createProfileAtAuthentication(customer);
            log.info("Controller: Made profile at Authentication");
            service.createProfileAtAccount(customer);
            log.info("Controller: Made profile at Account");

        } catch (Exception e) {
            try {
                service.emergencyDelete(customer);
            } catch (Exception e2) {
                log.error("Controller: " + e2.getMessage());
            }
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

    @PostMapping("/create-dummy-data")
    public String createDummyData(Model model) {
        log.info("Controller: Attempting to create dummy data");
        Customer customer = new Customer(
                "temp", "id", "Ola",
                "Nordmann", "address", "11223344",
                "ola@nordmann.no", "password"
        );

        try {
            if (service.checkIfAccountExist(customer.getPersonalId())) {
                customer = Instancio.create(Customer.class);
                customer.setEmail("demo@demo.no");
            }

            service.createProfileAtCustomer(customer);
            customer.setInternalId(service.getInternalId(customer.getPersonalId()));
            log.info("Controller: Made profile at Customer");
            service.createProfileAtAuthentication(customer);
            log.info("Controller: Made profile at Authentication");
            service.createProfileAtAccount(customer);
            log.info("Controller: Made profile at Account");

        } catch (Exception e) {
            try {
                service.emergencyDelete(customer);
            } catch (Exception e2) {
                log.error("Controller: " + e2.getMessage());
            }
            log.warn("Controller: " + e.getMessage());
            model.addAttribute("isSuccessful", false);
            model.addAttribute("message", "Failed: " + e.getMessage());
            return "create-account/response";
        }

        log.info("Controller: Successfully made account");
        model.addAttribute("isSuccessful", true);
        model.addAttribute("message",
                "Successfully made account" +
                "Username: " + customer.getPersonalId() + "   " +
                "Password: " + customer.getPassword() + "   "
        );
        return "create-account/response";
    }


}
