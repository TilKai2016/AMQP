package com.tilkai.demo.collect.server;

import com.tilkai.demo.collect.common.model.TestModel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消息的消费端
 * 参考Spring-AMQP：http://spring.io/blog/2015/05/08/spring-amqp-1-4-5-release-and-1-5-0-m1-available
 * @RabbitListener配合@RabbitHandler，允许一个Listener在一个类中根据不同的@Payload类型调用不同的方法；
 * @RabbitListener可以自动声明队列并绑定
 * @author tilkai
 */
@Component
@RabbitListener(queues = "demo.data")
public class DataCollectListener {

    @RabbitHandler
    public void process(@Payload String foo) {System.out.println(new Date() + ":" + foo);}

    @RabbitHandler
    public void process(@Payload TestModel model) {
        System.out.println(model.toString());
    }

    @RabbitHandler
    public String process(@Payload Integer value) {return value.toString();}

}
