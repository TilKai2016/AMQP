package com.tilkai.demo.collect.client;

import com.tilkai.demo.collect.common.model.TestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by tilkai on 2017/2/7.
 */
@Configuration
@EnableScheduling
public class Client {
    @Bean
    public Sender mySender() {
        return new Sender();
    }

    public class Sender {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Scheduled(fixedDelay = 1000L)
        public void send() {
            this.rabbitTemplate.convertAndSend("demo.data", "hello");
            this.rabbitTemplate.convertAndSend("demo.data", new TestModel("hello", "world"));
            System.out.println(this.rabbitTemplate.convertSendAndReceive("demo.data", 100));
        }
    }
}
