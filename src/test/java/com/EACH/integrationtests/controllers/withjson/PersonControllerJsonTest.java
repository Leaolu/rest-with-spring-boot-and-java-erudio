package com.EACH.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.EACH.Security.Util.UserDTO;
import com.EACH.configs.TestConfigs;
import com.EACH.data.vo.v1.PersonDTO;
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
public class PersonControllerJsonTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
	private static PersonDTO person;
	private static UserDTO user;
	private static String key;
	
	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonDTO();
		user = new UserDTO();
	}
	
	@AfterAll
	static void deleteUser() {
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/auth/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given(specification)
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.body(user)
		.delete("/delete")
		.then()
		.statusCode(204);
	}
	
	@Test
	@Order(1)
	void getAutho() throws JsonMappingException, JsonProcessingException{
		user.setUserName("Name");
		user.setPassword("OI");
			
			specification = new RequestSpecBuilder()
					.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
					.setBasePath("/api/auth/v1")
					.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
					.build();
			
					given(specification)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.body(user)
					.post("/signUp")
					.then()
					.statusCode(200);
					
					key = given(specification)
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.body(user)
							.post("/signIn")
							.then()
							.statusCode(200)
							.extract()
							.body()
							.asString().split(",")[0];
					
					
	}

	@Test
	@Order(2)
	void createTest() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, key)
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content = given(specification)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(person)
			.when()
				.post()
			.then()
					.statusCode(201)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getKey());
		assertTrue(createdPerson.getKey()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Jumiko", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}
	
	@Test
	@Order(3)
	void updateTest() throws JsonMappingException, JsonProcessingException {
		person.setLastName("Hyuga");
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getKey())
				.body(person)
				.when()
				.put("/{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getKey());
		assertTrue(createdPerson.getKey()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Hyuga", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}
	
	
	@Test
	@Order(4)
	void findByIdTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getKey())
			.when()
				.get("/{id}")
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getKey());
		assertTrue(createdPerson.getKey()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Hyuga", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}
	@Test
	@Order(5)
	void disableTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getKey())
				.when()
				.patch("/{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getKey());
		assertTrue(createdPerson.getKey()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Hyuga", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertFalse(createdPerson.getEnabled());
	}
	@Test
	@Order(6)
	void deleteTest() throws JsonMappingException, JsonProcessingException {
		given(specification)
				.pathParam("id", person.getKey())
				.when()
				.delete("/{id}")
				.then()
				.statusCode(204);
	}
	
	
	private void mockPerson() {
		person.setFirstName("Hanabi");
		person.setLastName("Jumiko");
		person.setAddress("Tokyo-Japan");
		person.setGender("Female");
		person.setEnabled(true);
		
	}
}
