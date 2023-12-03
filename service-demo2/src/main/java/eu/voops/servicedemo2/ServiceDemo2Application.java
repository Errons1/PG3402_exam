package eu.voops.servicedemo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.tracing.ConditionalOnEnabledTracing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@ConditionalOnEnabledTracing
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceDemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDemo2Application.class, args);
    }

}
