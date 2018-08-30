# 服务发现、注册中心 Eureka Server

官方文档：http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#spring-cloud-eureka-server

## 构建 Eureka 服务端

新建项目，依赖于
- Eureka Server
- Acutator
- Config Client

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
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

启用 Eureka Server
```java
@EnableEurekaServer
```

**bootstrap.properties**
```properties
spring.application.name=eureka-server
spring.cloud.config.uri=http://localhost:8888
```

在配置服务仓库中创建 **eureka-server.properties**
```properties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.server.enable-self-preservation=false
eureka.instance.prefer-ip-address=true
server.port=${PORT:8761}
#server.port=8761
```

启动后有个 WARN
```
WARN 81632 --- [           main] c.n.c.sources.URLConfigurationSource     : No URLs will be polled as dynamic configuration sources.
```
是因为 Hystrix 一个小 Bug，在 resources 目录下放一个空的 **config.properties** 就好了，见 [issue275](https://github.com/Netflix/Hystrix/issues/275)

TODO: 后台有个告警
```
2018-08-10 23:49:45.060  WARN 59232 --- [nio-8761-exec-1] c.n.e.registry.AbstractInstanceRegistry  : DS: Registry: lease doesn't exist, registering resource: CONFIG-SERVER - windows10.microdone.cn:config-server:8888
2018-08-10 23:49:45.060  WARN 59232 --- [nio-8761-exec-1] c.n.eureka.resources.InstanceResource    : Not Found (Renew): CONFIG-SERVER - windows10.microdone.cn:config-server:8888
```

## 多节点 Eureka

新建配置文件
**eureka-server-peer1.properties** 和 **eureka-server-peer2.properties**

设置两个新的启动配置，`Active Profile` 为 `peer1` 和 `peer2`

重启就好了，可以设置自己注册，也最好设置 Eureka Server 注册到对方服务器上

客户端会自动注册到两个 Eureka server 上，还最好设置客户端同时注册到两个 Eureka Server 上：
```properties
eureka.client.service-url.default-zone=http://localhost:8761/eureka,http://localhost:8762/eureka
```

可以设置 hosts 来用域名访问
```
127.0.0.1 peer1 peer2 peer3
```

```properties
eureka.client.service-url.default-zone=http://peer1:8761/eureka,http://peer2:8762/eureka
```

## 客户端连接

官方文档：http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#_service_discovery_eureka_clients

依赖
- Eureka Client
- Actuator

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

在客户端配置文件 **application.properties** 中，如果配置了配置中心，在配置中心的配置文件里，设置：
```properties
server.port=${PORT:9999}
eureka.client.service-url.default-zone=http://localhost:8761/eureka,http://localhost:8762/eureka
eureka.instance.prefer-ip-address=true
management.endpoints.web.exposure.include=health, info
eureka.client.healthcheck.enabled=true
```

- 需要打开管理端点 info, health
- 建议使用 prefer-ip-address，docker 环境可能还需要[设置 ip 和忽略的网卡](https://github.com/spring-cloud/spring-cloud-netflix/issues/1646#issuecomment-357213927)
- 设置 Health 精确检查
