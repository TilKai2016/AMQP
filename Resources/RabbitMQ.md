## RabbitMQ+Docker

参考：
[docker环境下的RabbitMQ部署，Spring AMQP使用](http://www.jianshu.com/p/c40166cb4e86)
[RabbitMQ在分布式系统中的应用](http://www.jianshu.com/p/f2d3c544d3c7)
[rabbitMQ site:www.jianshu.com](http://www.jianshu.com/p/a5f7fce67803)

### Docker安装

### RabbitMQ镜像

```
# 查找rabbitMQ镜像
docker search rabbitmq
```

```
# 下载rabbitMQ镜像(不带web管理插件的镜像)
docker pull rabbitmq
# 下载rabbitMQ镜像(带web管理插件的镜像)
docker pull rabbitmq:management
```

```
# 查看下载的镜像
docker images
```

```
# 启动rabbitMQ镜像(不带web管理插件)
docker run -d --publish 5671:5671 rabbitmq
# 启动rabbitMQ(带web管理插件)
docker run -d --publish 5671:5671 \
 --publish 5672:5672 --publish 4369:4369 --publish 25672:25672 --publish 15671:15671 --publish 15672:15672 \
rabbitmq:management
```

关于RabbitMQ的几个内部端口代表的意义：

```
4369:epmd(Erlang Port Mapper Daemon)相当于DNS作用
25672:Erlang distribution
5672, 5671:AMQP 0-9-1 without and with TLS
15672:if management plugin is enabled
61613, 61614:if STOMP is enabled
1883, 8883:if MQTT is enabled:
```

```
# 查看端口占用
lsof -i tcp:5671
```


带管理插件的RabbitMQ启动后可访问`http://localhost:15672/`，默认的用户名为`guest`密码为`guest`

## 关于Exchange 交换机机制

>The core idea in the messaging model in RabbitMQ is that the producer never sends any messages directly to a queue. Actually, quite often the producer doesn't even know if a message will be delivered to any queue at all.

>Instead, the producer can only send messages to an exchange. An exchange is a very simple thing. On one side it receives messages from producers and the other side it pushes them to queues. The exchange must know exactly what to do with a message it receives. Should it be appended to a particular queue? Should it be appended to many queues? Or should it get discarded. The rules for that are defined by the exchange type.

**exchange和queue通过routing-key关联(即binding)，exchange负责把message路由到不同的queue中。**

### Direct Exchange (路由键交换机)

>A direct exchange delivers messages to queues based on a message routing key. The routing key is a message attribute added into the message header by the producer. The routing key can be seen as an "address" that the exchange use to decide how to route the message. A message goes to the queue(s) whose binding key exactly matches the routing key of the message.

该交换机收到消息后会把消息发送到指定routing-key的queue中。那消息交换机是怎么知道的呢？其实，producer deliver消息的时候会把routing-key add到 message header中。routing-key只是一个messgae的attribute。

### Default Exchange (特殊的Direct Exchange)

rabbitmq内部默认的一个交换机。该交换机的name是空字符串，所有queue都默认binding 到该交换机上。所有binding到该交换机上的queue，routing-key都和queue的name一样。

### Topic Exchange (通配符交换机)

通配符交换机，exchange会把消息发送到一个或者多个满足通配符规则的routing-key的queue。其中\*匹配一个word，#匹配多个word和路径，路径之间通过.隔开。如满足a.\*.c的routing-key有a.hello.c；满足#.hello的routing-key有a.b.c.helo。

### fanout Exchange (扇形交换机)

>The fanout copies and routes a received message to all queues that are bound to it regardless of routing keys or pattern matching as with direct and topic exchanges. Keys provided will simply be ignored.

通常用作广播，所有该exchagne上指定的routing-key都会被ignore。

### Header Exchange

设置header attribute参数类型的交换机。

## SpringBoot中对RabbitMQ的注解支持

参考GitHub：[pzxwhc/MineKnowContainer](https://github.com/pzxwhc/MineKnowContainer/issues/48)

### @EnableRabbit

`@EnableRabbit`:
Enable Rabbit listener annotated endpoints that are created under the cover by a RabbitListenerContainerFactory. To be used on Configuration classes as follows:

```
@Configuration
@EnableRabbit
public class AppConfig {
    @Bean
    public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMaxConcurrentConsumers(5);
        return factory;
    }
    // other @Bean definitions
}
```

**The RabbitListenerContainerFactory is responsible to create the listener container responsible for a particular endpoint.**

Typical implementations, as the SimpleRabbitListenerContainerFactory used in the sample above, provides the necessary configuration options that are supported by the underlying MessageListenerContainer.

@EnableRabbit enables detection of RabbitListener annotations on any Spring-managed bean in the container

也就是说，@EnableRabbit 有两个功能：

* @Configuration，@EnableRabbit 配合使用，并且在该类下返回 RabbitListenerContainerFactory 类型的实体，如 SimpleRabbitListenerContainerFactory，那么该终端就能够监听队列。
* 可以自动检测 @RabbitListener 注解。

### @RabbitListener

`@RabbitListener`:
注解定义如下：

```
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@MessageMapping
@Documented
public @interface RabbitListener {
    String id() default "";
    String containerFactory() default "";
    String[] queues() default {};
    boolean exclusive() default false;
    String priority() default "";
    String admin() default "";
    QueueBinding[] bindings() default {};
    String group() default "";
}
```

从定义可以看出其中作用，例如，可以利用 queues 定义队列，bindings定义绑定。如下代码:

```
@RabbitListener(
    containerFactory = "myConnectionFactory",
    bindings = @QueueBinding(
        value = @Queue(value = "testFanoutQueue2",durable = "true"),
        exchange = @Exchange(value = "testFanoutExchange",type = ExchangeTypes.FANOUT))
)
```

### @RabbitHandler

`@RabbitHandler`:

```
 * Annotation that marks a method to be the target of a Rabbit message
 * listener within a class that is annotated with {@link RabbitListener}.
 *
 * <p>See the {@link RabbitListener} for information about permitted method signatures
 * and available parameters.
 * <p><b>It is important to understand that when a message arrives, the method selection
 * depends on the payload type. The type is matched with a single non-annotated parameter,
 * or one that is annotated with {@code @Payload}.
 * There must be no ambiguity - the system
 * must be able to select exactly one method based on the payload type.</b>
```

那么，可以利用 @RabbitListener 和 @RabbitHandler 结合，根据参数类型的不同，不同的消息就可以用不同的方法来进行处理。如下代码:

```
@Slf4j
@Component
@RabbitListener(containerFactory = "helloRabbitListenerContainer",queues = {"spring-boot","spring-boot1"})
public class Receiver {
    @RabbitHandler
    public void receiveTeacher(Teacher teacher) {
        log.info("#####1 = {}",teacher);
    }

    @RabbitHandler
    public void receiveStudent(Student student) {
        log.info("#####2 = {}",student);
    }
}
```

那么，这里需要注意的是，当 message 消息从 rabbitmq 来到 springboot 中的时候，是以 byte 字节的形式，那么，为什么能转换到 Student 类型活着 Teacher 类型呢，是因为需要有消息转换器，具体在 MessagingMessageListenerAdapter 类中的 onMessage 方法中：

```
Message<?> message = toMessagingMessage(amqpMessage);
```

## RabbitMQ的消息确认机制

[RabbitMQ：Publisher的消息确认机制 #49](https://github.com/pzxwhc/MineKnowContainer/issues/49)



