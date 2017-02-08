package com.tilkai.demo.collect.client;

import com.tilkai.demo.collect.common.model.TestModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 消息提供端
 *
 * @EnableScheduling 开启Spring对计划任务的支持，然后在要执行的计划任务方法上加上@Scheduled注解，声明这是个计划任务；
 * @Scheduled 在Spring中通过@Scheduled可以支持多种类型的计划任务，如fixedDelay、cron、fixedRate等；
 * fixedDelay：以上次调用结束和下次调用开始之间的固定周期执行被注解的方法；
 * fixedRate：指定每隔固定时间执行
 * cron：指定固定时间点执行
 *
 * @author tilkai
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
            this.rabbitTemplate.convertAndSend("demo.foo", "hello, foo!");
            this.rabbitTemplate.convertAndSend("demo.foo", new TestModel("hello", "world"));
            System.out.println(this.rabbitTemplate.convertSendAndReceive("demo.foo", 100));

            this.rabbitTemplate.convertAndSend("demo.ti", "hello, ti!");
        }
    }
}
