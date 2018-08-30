
# Hystrix

> 熔断器，容错管理工具，旨在通过熔断机制控制服务和第三方库的节点,从而对延迟和故障提供更强大的容错能力。

## Hystrix Dashboard

Hystrix 控制台：http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#_circuit_breaker_hystrix_dashboard

依赖
- Eureka Discovery
- Config Client
- Actuator
- Hystrix Dashboard
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**bootstrap.properties**
```
spring.cloud.config.uri=http://localhost:8888
spring.application.name=hystrix-dashboard
```

```java
@EnableHystrixDashboard
@SpringBootApplication
public class HystrixDashboardApplication {
```

配置中心 **hystrix-dashboard.properties**
```properties
server.port=${PORT:7001}
eureka.client.service-url.defaultZone=http://localhost:8761/eureka,http://localhost:8762/eureka
eureka.instance.prefer-ip-address=true
```

访问：
http://localhost:7001/hystrix

## 客户端熔断

在客户端应用内启用熔断器

依赖
- Hystrix
- Actuator
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

还要配置开放 actuator 端点 `management.endpoints.web.exposure.include=hystrix.stream`

启用熔断
```
@EnableHystrix
```

在需要熔断服务的方法设置
```
@HystrixCommand
```

设置错误转发
```
@HystrixCommand(fallbackMethod = "helloFallback")
```

再定义一个 **helloFallback** 方法来返回断路时的调用实现

访问 Hystrix Dashboard 地址 http://localhost:7001/hystrix ，在里面把地址：http://localhost:9999/actuator/hystrix.stream 添加进去作为监控对象（这个地址是会不断传入数据，注意不要在word里面打开）

TODO: 怎么打开断路器

### 监控数据 Redis

TODO: 