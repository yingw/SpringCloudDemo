# Turbine Service

>Turbine是聚合服务器发送事件流数据的一个工具，用来监控集群下hystrix的metrics情况。

新建项目，依赖
- Eureka Discovery
- Config Client
- Actuator
- Turbine

```
@EnableTurbine
```

**bootstrap.properties**
```properties
spring.application.name=turbine-service
spring.cloud.config.uri=http://localhost:8888,http://localhost:8889
```

**application.propertes**
```properties
turbine.aggregator.cluster-config=DEMO-SERVICE
turbine.app-config=demo-service
```
设置 cluster-config 属性为 Eureka Server 上注册的 Service 名称（大写），作为监控集群的名称，多个实例即可通过在 Hystrix Dashboard 监控：http://localhost:7099/turbine.stream?cluster=DEMO-SERVICE 来实现

如果要监控所有的 Hystrix 端点
```properties
turbine.app-config=demo-service,demo-client
turbine.cluster-name-expression='default'
```
监控路径：http://localhost:7099/turbine.stream 即可

## 注册到 Consul

注意 Turbine 的 starter 依赖于一个 Eureka Client 的 starter，会导致切换到 Consul 后，启动时有个找不到 Eureka 的报错，
根据[官网的描述](https://github.com/spring-cloud/spring-cloud-consul/issues/53)，改依赖应该在 B 版本已经被去掉了，并且应该用不是 starter 的依赖：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-netflix-turbine</artifactId>
</dependency>
```
另：官网 Turbine 和 Consul 的[说明](https://github.com/spring-cloud/spring-cloud-consul/blob/master/docs/src/main/asciidoc/spring-cloud-consul.adoc#circuit-breaker-with-hystrix)

也可能是个新的 bug，可以简单去掉依赖解决
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-turbine</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
