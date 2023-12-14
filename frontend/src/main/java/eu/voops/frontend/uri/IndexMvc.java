package eu.voops.frontend.uri;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexMvc {

    IndexService indexService;

    @GetMapping("")
    public String index(Model model,
                        @CookieValue(value = "internalId", defaultValue = "") String internalIdCookie,
                        @CookieValue(value = "isLoggedIn", defaultValue = "") String isLoggedInCookie
    ) {
        if (!internalIdCookie.isEmpty() || !isLoggedInCookie.isEmpty()) {
            log.info("Serving accounts");
            model.addAttribute("isAccounts", true);
            return "index";
        }

        log.info("Serving index");
        return "index";

    }

}
