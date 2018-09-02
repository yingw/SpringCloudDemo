package cn.yinguowei.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApplication.class, args);
    }

/*
    @Bean
    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, new DiscoveryLocatorProperties());
    }
*/

/*
    @Bean
    public RouteLocator customRouteLocator(ThrottleWebFilterFactory throttle) {
        return Routes.locator()
                .route("test")
                .uri("http://httpbin.org:80")
                .predicate(host("**.abc.org").and(path("/image/png")))
                .addResponseHeader("X-TestHeader", "foobar")
                .and()
                .route("test2")
                .uri("http://httpbin.org:80")
                .predicate(path("/image/webp"))
                .add(addResponseHeader("X-AnotherHeader", "baz"))
                .and()
                .build();
    }
*/

/*
    @Bean public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(t -> t.path("/demo-producer").and().uri("http://localhost:9000"))
                .build();
    }
*/
}
