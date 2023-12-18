package eu.voops.frontend.uri;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexMvc {
    
    /**
     * Retrieves the index page.
     *
     * @param model The model object to hold data for the view.
     * @param internalIdCookie The value of the 'internalId' cookie, or an empty string if not present.
     * @param isLoggedInCookie The value of the 'isLoggedIn' cookie, or an empty string if not present.
     * @return The name of the view to render for the index page.
     */
    @GetMapping("")
    public String index(Model model,
                        @CookieValue(value = "internalId", defaultValue = "") String internalIdCookie,
                        @CookieValue(value = "isLoggedIn", defaultValue = "") String isLoggedInCookie
    ) {
        log.info("Controller Attempting to server index");
        
        if (!internalIdCookie.isEmpty() && !isLoggedInCookie.isEmpty()) {
            model.addAttribute("isAccounts", true);
            log.info("Controller: Serving accounts");
            return "index";
        }

        log.info("Serving index");
        return "index";
    }

}
