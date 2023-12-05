package eu.voops.frontend;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@AllArgsConstructor
@Controller
public class MVController {

    MVCService mvcService;
    
    @GetMapping("/")
    public String index() {
        log.info("Serving index");  
        return "index";
    }
    
    @GetMapping("/demo")
    @ResponseBody
    public String demo() {
        log.info("Running demo");
        return mvcService.demo(); 
    }

}
