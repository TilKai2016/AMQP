package com.tilkai.demo.collect.client.config;

import com.oracle.webservices.internal.api.databinding.Databinding;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tilkai
 */
@Configuration
public class AmqpConfig {
    @Bean
    MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    DirectExchange directExchange() {
        DirectExchange directExchange = new DirectExchange("directExchange");
        return directExchange;
    }

    @Bean
    Queue fooQueue() {
        return new Queue("demo.foo");
    }

    @Bean
    Queue tiQueue() {
        return new Queue("demo.ti");
    }

    @Bean
    Binding fooBinding() {
        return BindingBuilder.bind(fooQueue()).to(directExchange()).with("fooRoutingKey");
    }

    @Bean
    Binding tiBinding() {
        return BindingBuilder.bind(tiQueue()).to(directExchange()).with("tiRoutingKey");
    }
}
