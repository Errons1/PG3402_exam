package eu.voops.servicedemo1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class AMQPConfig {

    @Bean
    public TopicExchange demo1TopicExchange(@Value("exchange.demo1") final String exchangeName) {
        return ExchangeBuilder.topicExchange("exchange.demo1").durable(true).build();
    }

    @Bean
    public Queue demo1Queue(@Value("queue.demo1") final String queueName) {
        return QueueBuilder.durable("queue.demo1").build();
    }

    @Bean
    public Binding demo1Binding(final Queue demo1Queue, TopicExchange demo1TopicExchange) {
        return BindingBuilder.bind(demo1Queue).to(demo1TopicExchange).with("key");
    }

}
