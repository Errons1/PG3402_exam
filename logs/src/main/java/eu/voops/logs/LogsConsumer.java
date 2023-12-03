package eu.voops.logs;

import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogsConsumer {

    @RabbitListener(queues = "logs.queue")
    @RabbitHandler
    public void log(final String msg, @Header("level") String level, @Header("amqp_appId") String appId, @Header(value = "traceId", required = false) String traceId, @Header(value = "spanId", required = false) String spanId) {
        Marker marker = MarkerFactory.getMarker(appId);
        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);


        switch (level) {
            case "INFO" -> log.info(marker, msg);
            case "ERROR" -> log.error(marker, msg);
            case "WARN" -> log.warn(marker, msg);
        }
    }
}
