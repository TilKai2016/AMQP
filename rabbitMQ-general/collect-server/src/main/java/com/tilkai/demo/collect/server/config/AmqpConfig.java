package com.tilkai.demo.collect.server.config;

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
    public Queue fooQueue() {
        return new Queue("demo.foo"); // 该构造函数实例化一个持久存在的，不排他的，不自动删除的队列。所谓不排他即其他用户可见、可访问的队列。
    }

    @Bean
    public Queue tiQueue() {return new Queue("demo.ti");}
}
