package top.sleepnano.sso.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableFeignClients(basePackages = {"top.sleepnano.kkcommon.service"})
@EnableResourceServer
public class ResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class,args);
    }
}
