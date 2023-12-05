package eu.voops.servicedemo2.configuration;

import brave.Tracing;
import brave.http.HttpTracing;
import brave.spring.web.TracingClientHttpRequestInterceptor;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Demo2Config {
    
    @Bean
    public ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer() {
        return (container -> container.setObservationEnabled(true));
    }

    @Bean
    public HttpTracing create(Tracing tracing) {
        return HttpTracing.newBuilder(tracing).build();
    }
    
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(HttpTracing httpTracing) {
        return new RestTemplateBuilder().interceptors(TracingClientHttpRequestInterceptor.create(httpTracing)).build();
    }

}
