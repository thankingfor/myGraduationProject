package vip.bzsy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author lyf
 * @create 2019-03-29 17:11
 */
@MapperScan(basePackages = "vip.bzsy.mapper")
@SpringBootApplication
public class CarSetApplication  extends SpringBootServletInitializer{
    public static void main(String[] args) {

        SpringApplication.run(CarSetApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CarSetApplication.class);
    }
}
