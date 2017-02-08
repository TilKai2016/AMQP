package com.tilkai.demo.collect.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

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

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
