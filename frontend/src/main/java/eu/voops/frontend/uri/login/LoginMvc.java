package eu.voops.frontend.uri.login;

import eu.voops.frontend.dto.DtoLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginMvc {

    LoginService service;

    @GetMapping("")
    public String login() {
        return "login/form";
    }

    @PostMapping("")
    public String login(@Valid @NonNull DtoLogin dto, Model model,
                        HttpServletResponse response,
                        @CookieValue(value = "internalId", defaultValue = "") String internalIdCookie,
                        @CookieValue(value = "isLoggedIn", defaultValue = "") String isLoggedInCookie
    ) {
        log.info("Controller: Attempting to login");
        dto = new DtoLogin(dto.getPersonalId(), dto.getPassword());
        boolean isLoggedIn = service.login(dto);

        if (!isLoggedIn) {
            log.warn("Failed to login");
            model.addAttribute("isSuccessful", false);
            model.addAttribute("message", "Failed to login");
            return "login/error";
        }
        
        String internalId = service.getInternalId(dto.getPersonalId());
        if (isLoggedInCookie.isEmpty() || internalIdCookie.isEmpty()) {
            final int hour = 60 * 60;
            
            Cookie cookie1 = new Cookie("internalId", internalId);
            cookie1.setHttpOnly(true); // Mitigate XSS attacks
            cookie1.setMaxAge(hour);  // Cookie will be valid for 1 hour 
            response.addCookie(cookie1);

            Cookie cookie2 = new Cookie("isLoggedIn", "true");
            cookie2.setHttpOnly(true);
            cookie2.setMaxAge(hour);
            response.addCookie(cookie2);

            log.info("Successful login");
            return "login/redirect";
        }

        log.info("Successful login??");
        return "login/redirect";
    }
}