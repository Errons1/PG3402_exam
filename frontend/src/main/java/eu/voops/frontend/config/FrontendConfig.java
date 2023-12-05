package eu.voops.frontend.config;

import brave.Tracing;
import brave.http.HttpTracing;
import brave.spring.web.TracingClientHttpRequestInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
@AllArgsConstructor
public class FrontendConfig {

    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void setup() {
        rabbitTemplate.setObservationEnabled(true);
    }

    @Bean
    public HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.newBuilder(tracing).build();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(HttpTracing httpTracing) {
        return new RestTemplateBuilder().interceptors(TracingClientHttpRequestInterceptor.create(httpTracing)).build();
    }
    
}
