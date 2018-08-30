
# Demo Service

依赖
- JPA
- H2
- Eureka Discovery
- Config Client
- Actuator
- Web

**bootstrap.properties**
```properties
spring.application.name=demo-service
spring.cloud.config.uri=http://localhost:8888
```

配置中心，**config-repo/demo-service.properties**
```
server.port=${PORT:9000}
eureka.client.service-url.default-zone=http://localhost:8761/eureka
service.user.name=John Doe
eureka.instance.prefer-ip-address=true
```

其他配置
```properties
info.build.name=@project.name@ 
info.build.description=@project.description@ 
info.build.groupId=${project.groupId}
info.build.artifactId=${project.artifactId}
info.build.version=${project.version}
management.endpoints.web.exposure.include=health, info, refresh, hystrix.stream
```

>要开放 info 等端点给注册中心

然后通过 RestController 开发点服务，通过 `@EnableHystrix` 启用，`@HystrixCommand` 设置熔断方法。

## 多节点

在 config-repo 新增加一个 **demo-service-node2.properties**，可以换一个端口或者修改其他属性
```properties
server.port=${PORT:9002}
eureka.client.service-url.default-zone=http://localhost:8761/eureka
service.user.name=Jane Doe
```

新增启动项，设置 Active Profiles = 'node2'，或者设置启动参数 spring.profiles.active='node2'

启动后两个节点都会注册到 Eureka 上

有时候重启节点会在 Eureka 上显示 DOWN (1) ，是服务停止过一次

TODO: 停掉其中一个节点后，客户端从 Eureka 一直拿不到另一个正确的实例，可能是断路器开了。

