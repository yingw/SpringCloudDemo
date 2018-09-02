package cn.yinguowei.demo.comsumer;

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
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/services")
    public Object services() {
        return discoveryClient.getInstances("demo-producer");
    }

    /**
     * 从所有服务中选择一个服务（轮询）
     */
    @RequestMapping("/discover")
    public Object discover() {
        return loadBalancerClient.choose("demo-producer").getUri().toString();
    }

    @HystrixCommand
    @RequestMapping("/")
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
}
