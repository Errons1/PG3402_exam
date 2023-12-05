package eu.voops.authentication;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@RestController()
@RequestMapping("/api/v1/")
public class AuthenticationController {

    private RestTemplate restTemplate;
    private RabbitTemplate rabbitTemplate;

}
