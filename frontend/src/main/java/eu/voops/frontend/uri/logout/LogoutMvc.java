package eu.voops.frontend.uri.logout;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/logout")
public class LogoutMvc {
    
    /**
     * Logs out the user by removing the appropriate cookies.
     *
     * @param response the HTTP servlet response
     * @return the view name for logging out
     */
    @GetMapping("")
    public String logout(HttpServletResponse response) {
        log.info("Controller: Attempting to logout");
        Cookie cookie1 = new Cookie("internalId", null);
        Cookie cookie2 = new Cookie("isLoggedIn", null);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
        log.info("Controller: Successfully logged out user");
        return "logout/logout";
    }
}
