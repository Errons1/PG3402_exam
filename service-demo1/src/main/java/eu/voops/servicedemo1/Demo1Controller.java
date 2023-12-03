package eu.voops.servicedemo1;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@AllArgsConstructor
public class Demo1Controller {

    private RestTemplate restTemplate;
    private RabbitTemplate rabbitTemplate;

//    @RabbitListener(queues = "queue.demo1")
//    @RabbitHandler
//    public void test(final String test) {
//        System.out.println(test);
//    }

    @GetMapping("/")
    public String helloWorld() {
        log.info("I will send rabbit message");
        rabbitTemplate.convertAndSend("exchange.demo1", "key", "asd");

        log.info("I will send REST message");
        String test = restTemplate.getForObject("http://demo2/demo2", String.class);
        return "Hello Demo1";
    }

}
