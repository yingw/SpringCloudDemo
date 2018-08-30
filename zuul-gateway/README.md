
```

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>

```

@EnableZuulProxy


```

zuul.routes.demo.path=/demo/**
#zuul.routes.demo.url=http://localhost:9999/
zuul.routes.demo.serviceId=demo-consumer
zuul.ignored-services=demo-producer,demo-consumer,api-gateway

```

自动映射，要把上面的注销掉