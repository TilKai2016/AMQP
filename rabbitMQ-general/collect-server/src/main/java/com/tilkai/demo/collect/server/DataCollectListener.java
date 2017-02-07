package com.tilkai.demo.collect.server;

import com.tilkai.demo.collect.common.model.TestModel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by tilkai on 2017/2/7.
 */
@Component
@RabbitListener(queues = "demo.data")
public class DataCollectListener {

    @RabbitHandler
    public void process(String foo) {System.out.println(new Date() + ":" + foo);}

    @RabbitHandler
    public void process(@Payload TestModel model) {
        System.out.println(model.toString());
    }

    @RabbitHandler
    public String process(Integer value) {return value.toString();}

}
