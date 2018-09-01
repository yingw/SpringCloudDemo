package cn.yinguowei.sbaserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SbaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbaServerApplication.class, args);
    }
}
