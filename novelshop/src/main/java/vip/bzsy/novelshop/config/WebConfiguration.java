package vip.bzsy.novelshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import vip.bzsy.novelshop.intercepter.JWTInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    //spring拦截器加载在springcontentText之前，所以这里用@Bean提前加载。否则会导致过滤器中的@AutoWired注入为空
    @Bean
    JWTInterceptor jwtInterceptor(){
        return new JWTInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        List<String> list = new ArrayList<>();
        list.add("/api/nov/login");
        list.add("/api/nov/register");
        list.add("/api/nov/getNewPublish");
        list.add("/api/nov/getRanking");
        list.add("/api/nov/getSearch");
        list.add("/api/nov/getNovelInfo");
        list.add("/api/nov/uploadChapter");
        list.add("/api/nov/download");
        list.add("/api/nov/getNovelChapter");
        list.add("/api/nov/getComment");
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/api/nov/**")
                .excludePathPatterns(list);
        super.addInterceptors(registry);
    }

    /**
     * cors做跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
