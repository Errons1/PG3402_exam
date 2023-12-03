package eu.voops.servicedemo2;

import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class Demo2Controller {

    private final AmqpTemplate amqpTemplate;

    @RabbitListener(queues = "queue.demo1")
    @RabbitHandler
    public void test(final String test) {
        System.out.println(test);
        log.info("Rabbit message received");
    }

    @ContinueSpan
    @GetMapping("/demo2")
    public String helloWorld() {
        log.info("REST message received");
        return "Hello Demo2";
    }
}
