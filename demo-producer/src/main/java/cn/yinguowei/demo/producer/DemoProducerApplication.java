package cn.yinguowei.demo.producer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication
public class DemoProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoProducerApplication.class, args);
    }

    @Value("${server.port}")
    String port;

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${demo.user.name}")
    String username;

    @HystrixCommand
    @RequestMapping("/")
//    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloService(@RequestParam(value = "name", required = false) String name) {
        return "Hello, I'm " + (name == null ? username : name) + ", and I came from " + applicationName + ":" + port;
    }

}
