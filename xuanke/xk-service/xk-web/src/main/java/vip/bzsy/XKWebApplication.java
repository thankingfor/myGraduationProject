package vip.bzsy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author lyf
 * @create 2019-03-20 21:32
 */
@EnableEurekaClient
@SpringBootApplication
public class XKWebApplication extends WebMvcConfigurationSupport{

    public static void main(String[] args) {

        SpringApplication.run(XKWebApplication.class, args);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

}
