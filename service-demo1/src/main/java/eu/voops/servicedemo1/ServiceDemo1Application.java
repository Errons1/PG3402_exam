package eu.voops.servicedemo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableJpaRepositories
@SpringBootApplication
public class ServiceDemo1Application {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDemo1Application.class, args);
	}

}
