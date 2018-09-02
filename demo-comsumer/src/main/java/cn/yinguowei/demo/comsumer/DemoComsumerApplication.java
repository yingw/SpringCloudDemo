package cn.yinguowei.demo.comsumer;

import com.google.common.base.Predicates;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@RestController
@SpringBootApplication
public class DemoComsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoComsumerApplication.class, args);
    }

/*
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
*/

    @Value("${demo.user.name}")
    String username;

    @Autowired HelloService helloService;
/*
    @Autowired RestTemplate restTemplate;
*/

/*
*/
    @Autowired LoadBalancerClient loadBalancerClient;
    @Autowired DiscoveryClient discoveryClient;

    /**
     * 获取所有服务
     */
    @GetMapping("/services")
    public Object services() {
        return discoveryClient.getInstances("demo-producer");
    }

    /**
     * 从所有服务中选择一个服务（轮询）
     */
    @GetMapping("/discover")
    public Object discover() {
        return loadBalancerClient.choose("demo-producer").getUri().toString();
    }

    @HystrixCommand
    @GetMapping("/")
    public String helloClient() {
        return helloService.hello(username);
//        return restTemplate.getForObject("http://demo-service/", String.class);
//        return "";
    }
/*
    public String helloFallback() {
        return "Error from hello()!";
    }
*/

    // Gateway 方式，设置 zuul-gateway /demo-producer
    @FeignClient(name = "demo-producer", fallback = HelloServiceFallback.class)
    interface HelloService {
        @GetMapping("/")
        String hello(@RequestParam("name") String name);
    }

    @Component
    class HelloServiceFallback implements HelloService {

        @Override
        public String hello(String name) {
            return "Error from hello(), name = " + name;
        }
    }

    @Bean
    public Docket api() { //BuildProperties build
        return new Docket(SWAGGER_2)
                .ignoredParameterTypes(Errors.class) //OAuth2Authentication.class,
//                .apiInfo(apiInfo(build))
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("cn.yinguowei"))
                .paths(Predicates.not(PathSelectors.ant("/actuator/**")))
                .build();
    }
}
