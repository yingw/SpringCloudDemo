# Spring Cloud Bus

- 官方文档：http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#_spring_cloud_bus
- http://www.ityouknow.com/springcloud/2017/05/26/springcloud-config-eureka-bus.html

给参与同步更新的服务客户端都增加 Spring Cloud Bus RabbitMQ 的依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>

```

开放 `/actuator/bus-refresh` 端点；配置 RabbitMQ 服务器地址
```properties
management.endpoints.web.exposure.include=health, info, refresh, bus-refresh, bus, env

spring.rabbitmq.host=10.229.255.77
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=admin123
```

重启后就能连接到 /actuator/bus-refresh 来刷新配置，并通过总线通知所有应用
```
curl -X POST http://localhost:8889/actuator/bus-refresh
```

## Docker 启动 RabbitMQ

```
docker run -d \
  --hostname my-rabbit --name rabbitmq \
  -p 15672:15672 -p 5672:5672 -P \
  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin123 \
  rabbitmq:3.7.7-management
```

访问管理控制台在：http://10.229.255.77:15672 admin/admin123
