

# Demo Client

依赖
- Eureka Discovery
- Config Client
- Feign
- Zuul
- Ribbon
- Hystrix
- Actuator
- Web
- Thymeleaf
- Zipkin Client


三种调用服务的方式
1. Feign
2. RestTemplate
3. DiscoveryClient

## RestTemplate
定义 RestTemplate
```java
@Bean
@LoadBalanced
RestTemplate restTemplate() {
    return new RestTemplate();
}
```

用 RestTemplate 调用远程接口
```java
restTemplate.getForObject("http://demo-service/?name=" + username, String.class);
```
>注意 `demo-service` 可以大写，也可以小写

## Refresh Config
`@RefreshScope`

执行 `curl -X POST http://localhost:9999/actuator/refresh`

## Ribbon

## Feign

>Feign是一种声明式、模板化的HTTP客户端。

声明开启 Feign
```
@EnableFeignClients
```

声明接口类 `HelloService`，对应方法 `@GetMapping("/") String hello();`

声明 FeignClient
```java
@FeignClient(value = "demo-service", fallback = HelloService.HelloServiceFallback.class)
```

定义 fallback 方法
```java
@Component
class HelloServiceFallback implements HelloService {
    @Override
    public String hello() {
        return "Error from hystrix!";
    }
}
```

最后在调用的类中注入 `HelloService` 就可以调用了。

## Zuul

>Zuul 是在云平台上提供动态路由,监控,弹性,安全等边缘服务的框架。Zuul 相当于是设备和 Netflix 流应用的 Web 网站后端所有请求的前门。

TODO:

## Spring Cloud Gateway

TODO:

## Message

TODO:


## Hystrix

TODO: 总有一个问题启动 Client 后第一次调用重视 Error，之后就好了，而且 Hystrix 的监控也会卡住。(Fixed)

设置断路器打开规则（开发环境）

hystrix.command.default.circuitBreaker.requestVolumeThreshold=3

TODO: 打开后没有调到 fallback 方法：com.netflix.client.ClientException: Load balancer does not have available server for client: demo-producer
