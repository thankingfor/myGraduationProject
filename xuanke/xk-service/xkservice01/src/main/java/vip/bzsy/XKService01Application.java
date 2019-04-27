package vip.bzsy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lyf
 * @create 2019-03-20 21:32
 */
@MapperScan(basePackages = "vip.bzsy.mapper")
@EnableEurekaClient
@SpringBootApplication
public class XKService01Application {
    public static void main(String[] args) {

        SpringApplication.run(XKService01Application.class, args);
    }
}
