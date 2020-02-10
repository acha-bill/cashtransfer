package com.cashtransfer;

import com.cashtransfer.config.DefaultAdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {

	@Autowired
	private DefaultAdminConfig defaultAdminConfig;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		defaultAdminConfig.setup();
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.cashtransfer.rest")).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
				.title("Rapid Transfer")
				.description("Transfer money internationally")
				.version("1.0.0")
				.build();
	}
}


