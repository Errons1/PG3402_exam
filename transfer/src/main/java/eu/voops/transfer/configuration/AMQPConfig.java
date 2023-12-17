package eu.voops.transfer.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class AMQPConfig {

    @Bean
    public TopicExchange transferTopicExchange(@Value("${rabbit.exchange.transfer}") final String exchangeName) {
        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Queue transferQueue(@Value("${rabbit.queue.transfer}") final String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding transferBinding(@Value("${rabbit.key.transfer}") final String key, 
                                   @Qualifier("transferQueue") Queue queue,
                                   @Qualifier("transferTopicExchange") TopicExchange topicExchange
    ) {
        return BindingBuilder.bind(queue).to(topicExchange).with(key);
    }

//    @Bean
//    public TopicExchange requestAccountTopicExchange(@Value("${rabbit.exchange.requestAccount}") final String exchangeName) {
//        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
//    }
//
//    @Bean
//    public Queue requestAccountQueue(@Value("${rabbit.queue.requestAccount}") final String queueName) {
//        return QueueBuilder.durable(queueName).build();
//    }
//
//    @Bean
//    public Binding requestAccountBinding(@Value("${rabbit.key.requestAccount}") final String key, 
//                                         @Qualifier("requestAccountQueue") Queue queue,
//                                         @Qualifier("requestAccountTopicExchange") TopicExchange topicExchange
//    ) {
//        return BindingBuilder.bind(queue).to(topicExchange).with(key);
//    }
//
//    @Bean
//    public TopicExchange receiveAccountTopicExchange(@Value("${rabbit.exchange.receiveAccount}") final String exchangeName) {
//        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
//    }
//
//    @Bean
//    public Queue receiveAccountQueue(@Value("${rabbit.queue.receiveAccount}") final String queueName) {
//        return QueueBuilder.durable(queueName).build();
//    }
//
//    @Bean
//    public Binding receiveAccountBinding(@Value("${rabbit.key.receiveAccount}") final String key, 
//                                  @Qualifier("receiveQueue") Queue queue,
//                                  @Qualifier("receiveTopicExchange") TopicExchange topicExchange
//    ) {
//        return BindingBuilder.bind(queue).to(topicExchange).with(key);
//    }

}
