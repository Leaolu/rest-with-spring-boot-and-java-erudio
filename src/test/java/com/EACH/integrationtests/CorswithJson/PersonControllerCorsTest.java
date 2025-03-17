package com.EACH.integrationtests.CorswithJson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

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
public class PersonControllerCorsTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
	private static PersonDTO person;
	
	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonDTO();
	}
	

	@Test
	@Order(1)
	void create() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		specification = new RequestSpecBuilder()
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
				.extract()
					.body()
						.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getKey());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		
		assertTrue(createdPerson.getKey()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Jumiko", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}
	
	@Test
	@Order(2)
	void createWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
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
					.statusCode(403)
				.extract()
					.body()
						.asString();
		
		assertEquals("Invalid CORS request", content);
	}
	
	@Test
	@Order(3)
	void findById() throws JsonMappingException, JsonProcessingException {
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content = given(specification)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getKey())
			.when()
				.get("/{id}")
			.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getKey());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		
		assertTrue(createdPerson.getKey()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Jumiko", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}
	@Test
	@Order(4)
	void findByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getKey())
				.when()
				.get("/{id}")
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();
		
		assertEquals("Invalid CORS request", content);
	}
	
	private void mockPerson() {
		person.setFirstName("Hanabi");
		person.setLastName("Jumiko");
		person.setAddress("Tokyo-Japan");
		person.setGender("Female");
		person.setEnabled(true);
		
	}
}
