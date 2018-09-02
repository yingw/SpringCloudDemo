package cn.yinguowei.demo.producer;

import com.google.common.base.Predicates;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableSwagger2
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

    @ApiOperation(value = "Say Hello Service")
    @HystrixCommand
    @GetMapping("/")
//    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloService(
            @ApiParam(name = "name", value = "姓名", defaultValue = "殷国伟")
            @RequestParam(value = "name", required = false) String name) {
        return "Hello, I'm " + (name == null ? username : name) + ", and I came from " + applicationName + ":" + port;
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
