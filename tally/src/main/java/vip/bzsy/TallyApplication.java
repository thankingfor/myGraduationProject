package vip.bzsy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author lyf
 * @create 2019-03-20 21:32
 */
@MapperScan(basePackages = "vip.bzsy.mapper")
@SpringBootApplication
public class TallyApplication extends SpringBootServletInitializer{
    public static void main(String[] args) {

        SpringApplication.run(TallyApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TallyApplication.class);
    }
}
