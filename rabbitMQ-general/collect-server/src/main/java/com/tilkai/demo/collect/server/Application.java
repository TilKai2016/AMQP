package com.tilkai.demo.collect.server;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * 由于@Configuration、@EnableAutoConfiguration、@ComponentScan三个注解经常一起使用来注解main方法所在类，
 * SpringBoot提供了统一注解@SpringBootApplication用于替代以上三个注解的组合。
 *
 * @Configuration：使用@Configuration和@Bean的注解组合就可以创建一个简单的Spring配置类来替代XML配置；
 * @EnableAutoConfiguration：自动配置Spring上下文
 * @ComponentScan：自动扫描指定包下全部标有@Component的类，并注册成bean(包括@Component下的子注解@Service、@Repository、@Controller)
 *
 * @author tilkai
 */
@SpringBootApplication
@EnableCaching
public class Application {

    final static String queueName = "spring-boot";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
