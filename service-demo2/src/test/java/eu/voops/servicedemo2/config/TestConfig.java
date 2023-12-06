package eu.voops.servicedemo2.config;

import brave.Tracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    public Tracing tracing() {
        return Tracing.newBuilder().build();
    }
    
}
