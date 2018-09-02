
加上 web zipkin hystrix 等会报错

在 chrome 里面跳转有点问题，会调到百度搜索页，用无痕模式可以。

```xml

<!-- Spring Cloud Gateway !!! -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

```
```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: google
        uri: http://google.com
        predicates:
        - Path=/google
      - id: baidu
        uri: http://baidu.com
        predicates:
        - Path=/baidu
```

```yaml

      - id: producer
        uri: http://localhost:9000
        predicates:
        - Path=/producer/**
      - id: cousumer
        uri: lb:demo-consumer
        predicates:
        - Path=/cousumer/**
```
uri以lb://开头（lb代表从注册中心获取服务）