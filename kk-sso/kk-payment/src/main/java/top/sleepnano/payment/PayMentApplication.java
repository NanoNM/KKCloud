package top.sleepnano.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("top.sleepnano.kkcommon.service")
public class PayMentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayMentApplication.class,args);
    }
}
