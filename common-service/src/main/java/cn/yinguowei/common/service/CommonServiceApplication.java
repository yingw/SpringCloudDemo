package cn.yinguowei.common.service;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.retry.annotation.EnableRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableFeignClients
//@EnableRetry
@EnableSwagger2
@RestController
@SpringBootApplication
@SpringCloudApplication
public class CommonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonServiceApplication.class, args);
    }

    @GetMapping("/test")
    @ApiOperation(value = "测试方法", notes = "这个是用于测试的方法 hello world")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回"),
            @ApiResponse(code = 401, message = "未授权")}
    )
    public String testEndpoint(
            @ApiParam(name = "name", value = "名称", required = true)
            @RequestParam(defaultValue = "") String name) {
        return "Hello, " + name;
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

    // TODO: https://springfox.github.io/springfox/docs/snapshot/
    private ApiInfo apiInfo(BuildProperties build) {
        ApiInfoBuilder builder = new ApiInfoBuilder().title(build.getName())
                .description("Orquesta requests para microservicios Customer on Boarding chile")
                .version(build.getVersion());
        return builder.build();
    }
}


