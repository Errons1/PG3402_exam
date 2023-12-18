package eu.voops.frontend.uri.login;

import eu.voops.frontend.dto.DtoLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginMvc {

    private final LoginService service;

    /**
     * Retrieves the login page.
     *
     * @return The name of the login form template.
     */
    @GetMapping("")
    public String login() {
        log.info("Controller: Serving login/form");
        return "login/form";
    }

    /**
     * Handles the login request and performs the login process.
     *
     * @param dto              The login data.
     * @param model            The model to use for rendering the view.
     * @param response         The HTTP response.
     * @param internalIdCookie The value of the "internalId" cookie.
     * @param isLoggedInCookie The value of the "isLoggedIn" cookie.
     * @return The name of the view to render.
     */
    @PostMapping("")
    public String login(@Valid @NonNull DtoLogin dto, Model model,
                        HttpServletResponse response,
                        @CookieValue(value = "internalId", defaultValue = "") String internalIdCookie,
                        @CookieValue(value = "isLoggedIn", defaultValue = "") String isLoggedInCookie
    ) {
        log.info("Controller: Attempting to login");
        boolean isLoggedIn = service.login(dto);

        if (!isLoggedIn) {
            model.addAttribute("isSuccessful", false);
            model.addAttribute("message", "Failed to login");
            log.warn("Controller: Failed to login");
            return "login/error";
        }
        
        String internalId = service.getInternalId(dto.getPersonalId());
        if (isLoggedInCookie.isEmpty() || internalIdCookie.isEmpty()) {
            final int timedCookie = 60 * 2; // The cookies will log you out after 2 min
            
            Cookie cookie1 = new Cookie("internalId", internalId);
            cookie1.setHttpOnly(true); // Mitigate XSS attacks
            cookie1.setMaxAge(timedCookie);  // Cookie will be valid for 1 hour 
            response.addCookie(cookie1);

            Cookie cookie2 = new Cookie("isLoggedIn", "true");
            cookie2.setHttpOnly(true);
            cookie2.setMaxAge(timedCookie);
            response.addCookie(cookie2);

            log.info("Controller: Successful login");
            return "login/redirect";
        }

        log.info("Controller: Successful login");
        return "login/redirect";
    }
}
