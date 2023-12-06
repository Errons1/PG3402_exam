package eu.voops.servicedemo1;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class Demo1Controller {

    private RestTemplate restTemplate;
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/")
    public String helloWorld() {
        log.info("I will send rabbit message");
        rabbitTemplate.convertAndSend("exchange.demo1", "key", "asd");

        log.info("I will send REST message");
        restTemplate.getForObject("http://demo2/api/v1/", String.class);
        return "Hello Demo1";
    }

}
