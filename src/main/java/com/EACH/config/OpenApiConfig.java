package com.EACH.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	
		//Documentation at localhost:8080/swagger-ui/index.hmtl
    @Bean
    OpenAPI customOpenAPI() {
			return new OpenAPI()
					.info(new Info()
							.title("RESTful API with Java 21 and Spring boot 3")
							.version("v1")
							.description("Web service RESTful API")
							.termsOfService("https://github.com/Leaolu/rest-with-spring-boot-and-java-erudio")
							.license(
									new License()
									.name("Apache 2.0")
									.url("https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui/1.8.0")
									)
							);
		}
}
