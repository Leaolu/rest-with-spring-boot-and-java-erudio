package com.EACH.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.EACH.Security.Util.UserDTO;
import com.EACH.configs.TestConfigs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import IntegrationTest.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerJsonTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static UserDTO user;
	
	private static String content;
	
	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
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
				.contentType(MediaType.APPLICATION_JSON_VALUE)
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
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/auth/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(user)
				.post("/signUp")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		
		assertNotNull(content);
	}
	
	@Test
	@Order(3)
	void SignIn() throws JsonMappingException, JsonProcessingException{
		
		given(specification)
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.body(user)
		.post("/signIn")
		.then()
		.statusCode(200)
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.extract()
		.body()
		.asString();
		
	}
	
	@Test
	@Order(4)
	void Delete() throws JsonMappingException, JsonProcessingException{
		given(specification)
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.body(user)
		.delete("/delete")
		.then()
		.statusCode(204);
	}
	
	
	
	
	private void mockUser() {
		user.setUserName("Luisa");
		user.setPassword("Password");
	}
	
}
