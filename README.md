
Eureka vs Consul vs JHipster Registry

Zuul vs Spring Cloud Gateway vs Kong

Apollo vs Spring Cloud Config vs Consul

JHipster UAA vs CloudFoundry UAA vs Keycloak

ELK vs JHipster Console

Java Client vs HTML Client

# 使用手册


## 注册中心

启动 Consul

```
consul.exe agent -dev
```

## 配置中心


## 服务配置刷新

执行
```
curl -X POST http://localhost:9000/actuator/refresh
```

## hystrix 流

http://localhost:9000/actuator/hystrix.stream

