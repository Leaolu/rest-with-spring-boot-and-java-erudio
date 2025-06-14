package com.EACH.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.EACH.serelization.converter.YamlJacksonToHTTPMessageConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");
	
	@Value("${cors.originPatterns:default}")
	private String corsOriginPatterns = "";
	
	
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		var allowedOrigin = corsOriginPatterns.split(",");
		registry.addMapping("/**")
		.allowedOrigins(allowedOrigin)
		.allowedMethods("*")
		//.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
		.allowCredentials(true);
	}



	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new YamlJacksonToHTTPMessageConverter());
	}



	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		//Via EXTENSION http://localhost:8080/api/person/v1.xml has been DEPRECATED on SpringBoot 2.6
		
	/*	Via QUERY PARAM http://localhost:8080api/person/v1?mediaType=xml
		configurer.favorParameter(true)
		.parameterName("mediaType").ignoreAcceptHeader(true)
		.useRegisteredExtensionsOnly(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("json", MediaType.APPLICATION_JSON)
		.mediaType("xml", MediaType.APPLICATION_XML);
		*/
		
		//Via HEADER PARAM http://localhost:8080api/person/v1
		configurer.favorParameter(false)
		.ignoreAcceptHeader(false)
		.useRegisteredExtensionsOnly(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("json", MediaType.APPLICATION_JSON)
		.mediaType("xml", MediaType.APPLICATION_XML)
		.mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);
		
		
	}

}
