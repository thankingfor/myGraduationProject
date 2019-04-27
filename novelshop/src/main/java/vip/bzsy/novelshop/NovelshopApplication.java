package vip.bzsy.novelshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan(basePackages = "vip.bzsy.novelshop.mapper")
@SpringBootApplication
public class NovelshopApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(NovelshopApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(NovelshopApplication.class);
	}
}
