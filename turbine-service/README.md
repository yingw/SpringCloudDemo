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