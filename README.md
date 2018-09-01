
Eureka vs Consul vs JHipster Registry

Zuul vs Spring Cloud Gateway vs Kong

Apollo vs Spring Cloud Config vs Consul

JHipster UAA vs CloudFoundry UAA vs Keycloak

ELK vs JHipster Console

Java Client vs HTML Client

# 使用手册

启动步骤

1. ConfigServer 会有报错
2. 注册中心

## 注册中心

启动 Consul

```
consul.exe agent -dev
```

### eureka

peer1 和 peer2 启动互相注册，会有报错。

## 配置中心

互相依赖问题，如果需要 config server 启动不报错，可以设置
eureka.client.enabled = false

启动好 eureka 后再重启 eureka.client.enabled = true

见：https://stackoverflow.com/questions/46311405/spring-cloud-config-server-circular-dependency-with-netflix-eureka-discovery-on

没有必要。

## 服务配置刷新

执行
```
curl -X POST http://localhost:9000/actuator/refresh
```

## hystrix 流

http://localhost:9000/actuator/hystrix.stream

## Zipkin Server

2.0 开始不建议内建 server 了

```
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar
```

version 2.11.4, 35m 左右

访问 http://localhost:9411