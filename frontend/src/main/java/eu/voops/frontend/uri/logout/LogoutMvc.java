package eu.voops.frontend.uri.logout;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/logout")
public class LogoutMvc {
    
    @GetMapping("")
    public String logout(HttpServletResponse response) {
        Cookie cookie1 = new Cookie("internalId", null);
        Cookie cookie2 = new Cookie("isLoggedIn", null);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
        return "logout/logout";
    }
}
