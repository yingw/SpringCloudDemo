package cn.yinguowei.swagger;

import com.spring4all.swagger.EnableSwagger2Doc;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EnableDiscoveryClient
@EnableSwagger2Doc
@EnableZuulProxy
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

@Primary
@Component
class DocumentConfig implements SwaggerResourcesProvider {
    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired DiscoveryClient discoveryClient;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        discoveryClient.getServices().forEach(System.out::println);

//        Map<String, ZuulProperties.ZuulRoute> routes = zuulProperties.getRoutes();
//        routes.keySet().stream().forEach(route -> resources.add(buildResource(route, routes.get(route).getLocation())));
        discoveryClient.getServices().forEach(name -> {
            resources.add(buildResource(name, "/" + name + "/v2/api-docs"));
        });
        return resources;
    }

    private SwaggerResource buildResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
//        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

/*
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("Demo Producer", "/demo-producer/v2/api-docs", "1.0"));
        resources.add(swaggerResource("Demo Consumer", "/demo-consumer/v2/api-docs", "1.0"));
        resources.add(swaggerResource("Common Service", "/common-service/v2/api-docs", "1.0"));
        resources.add(swaggerResource("Swagger Server", "/v2/api-docs", "1.0"));
        return resources;
    }
*/

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
