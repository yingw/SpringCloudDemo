package cn.yinguowei.swagger;

import com.spring4all.swagger.EnableSwagger2Doc;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@EnableSwagger2Doc
@SpringBootApplication
@RestController
public class SwaggerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerServerApplication.class, args);
    }

    @GetMapping("/test")
    @ApiOperation(value = "测试方法", notes = "这个是用于测试的方法 hello world")
/*
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回"),
            @ApiResponse(code = 401, message = "未授权"),
            @ApiResponse(code = 403, message = "禁止访问"),
            @ApiResponse(code = 404, message = "未找到")}
    )
*/
    public ResponseEntity<String> testEndpoint(
            @ApiParam(name = "name", value = "名称", required = true)
            @RequestParam(defaultValue = "") String name) {
        return new ResponseEntity<>("Hello, " + name, HttpStatus.OK);
    }
}
