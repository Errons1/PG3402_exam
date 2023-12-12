package eu.voops.frontend.mvc;

import eu.voops.frontend.service.IndexService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexMvc {

    IndexService indexService;
    
    @GetMapping("/")
    public String index() {
        log.info("Serving index");  
        return "index";
    }

}
