package eu.voops.transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.voops.transfer.dto.DtoTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/transfer")
public class TransferController {
    
    private final TransferService service;

    @RabbitListener(queues = "${rabbit.queue.transfer}")
    @RabbitHandler
    private void transfer(String json) throws Exception {
        DtoTransfer dtoTransfer = new ObjectMapper().readValue(json, DtoTransfer.class);
//        This sleep is here to simulate random that this service takes long while to execute its job
//        And that is the reason we are using ASYNC communication between frontend and transaction to send data.
        Thread.sleep((long) (Math.random() * 5 * 1000) + 1);
        service.processTransfer(dtoTransfer);
    }
    
}
