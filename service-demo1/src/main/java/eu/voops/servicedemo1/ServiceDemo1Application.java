package eu.voops.servicedemo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.tracing.ConditionalOnEnabledTracing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@ConditionalOnEnabledTracing
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceDemo1Application {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDemo1Application.class, args);
	}

}
