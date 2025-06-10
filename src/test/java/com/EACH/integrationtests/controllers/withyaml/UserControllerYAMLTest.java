package com.EACH.integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.EACH.Security.Util.UserDTO;
import com.EACH.configs.TestConfigs;
import com.EACH.util.MediaType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import IntegrationTest.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerYAMLTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	private static YAMLMapper objectMapper;
	private static UserDTO user;
	
	private static String content;
	private static String key;
	private static String refresh;
	
	@BeforeAll
	static void setUp() {
		objectMapper = new YAMLMapper();
        // Configure RestAssured to use YAMLMapper for both serialization and deserialization
        RestAssured.config = RestAssured.config()
        	    .encoderConfig(EncoderConfig.encoderConfig()
        	        .encodeContentTypeAs(com.EACH.util.MediaType.APPLICATION_YML, ContentType.TEXT))
        	    .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
        	        .jackson2ObjectMapperFactory((cls, charset) -> objectMapper));

		
		user = new UserDTO();
	}
	
	@Test
	@Order(1)
	void UnauthenticatedRequest() throws JsonMappingException, JsonProcessingException {
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given(specification)
				.contentType(com.EACH.util.MediaType.APPLICATION_YML)
					.pathParam("id", 9)
				.when()
					.get("/{id}")
				.then()
						.statusCode(401);
	}
	
	@Test
	@Order(2)
	void SignUp() throws JsonMappingException, JsonProcessingException {
		mockUser();
		uSpec();
		
		String requestBody = objectMapper.writeValueAsString(user);
		
		content = given(specification)
				.contentType(MediaType.APPLICATION_YML)
				.accept(MediaType.APPLICATION_YML)
				.body(requestBody)
				.post("/signUp")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
		
		assertNotNull(content);
	}
	
	@Test
	@Order(3)
	void SignIn() throws JsonMappingException, JsonProcessingException{
		
		String requestBody = objectMapper.writeValueAsString(user);
		
		refresh = given(specification)
		.contentType(MediaType.APPLICATION_YML)
		.accept(MediaType.APPLICATION_YML)
		.body(requestBody)
		.post("/signIn")
		.then()
		.statusCode(200)
		.contentType(MediaType.APPLICATION_YML)
		.extract()
		.body()
		.asString().split(",")[1];
	}
	
	@Test
	@Order(4)
	void refreshToken() throws JsonProcessingException, JsonMappingException {
		specification = new RequestSpecBuilder()
				.addHeader("refreshToken", refresh)
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/auth/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		key = given(specification)
				.contentType(MediaType.APPLICATION_YML)
				.accept(MediaType.APPLICATION_YML)
				.post("/refreshToken")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
	}
	
	@Test
	@Order(5)
	void authenticatedRequest() throws JsonMappingException, JsonProcessingException{
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, key)
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given(specification)
			.contentType(MediaType.APPLICATION_YML)
			.accept(MediaType.APPLICATION_YML)
			.get()
			.then()
			.statusCode(200);
	}
	
	@Test
	@Order(6)
	void Delete() throws JsonMappingException, JsonProcessingException{
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/auth/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		String requestBody = objectMapper.writeValueAsString(user);
		
		
		given(specification)
		.contentType(MediaType.APPLICATION_YML)
		.body(requestBody)
		.delete("/delete")
		.then()
		.statusCode(204);
	}
	
	
	
	
	private void mockUser() {
		user.setUserName("Luisa");
		user.setPassword("Password");
	}
	
	
	private void uSpec() {
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/auth/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
	}
	
}
