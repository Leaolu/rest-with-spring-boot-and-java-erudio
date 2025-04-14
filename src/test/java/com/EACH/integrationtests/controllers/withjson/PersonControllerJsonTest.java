package com.EACH.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.EACH.configs.TestConfigs;
import com.EACH.data.vo.v1.PersonDTO;
import com.EACH.integrationtests.DTO.wrapper.json.WrapperPersonDTO;
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
	
	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonDTO();
	}
	

	@Test
	@Order(1)
	void createTest() throws JsonMappingException, JsonProcessingException {
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
	@Order(2)
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
	@Order(3)
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
	@Order(4)
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
	@Order(5)
	void deleteTest() throws JsonMappingException, JsonProcessingException {
		given(specification)
				.pathParam("id", person.getKey())
				.when()
				.delete("/{id}")
				.then()
				.statusCode(204);
	}
	
	@Test
	@Order(6)
	void findByAllTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.queryParam("page", 3, "size", 12, "direction", "asc")
			.when()
				.get()
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();
		
		WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
		List<PersonDTO> people = wrapper.getEmbedded().getPeople();
		//List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});
		PersonDTO firstPerson = people.get(0);
		
		assertNotNull(firstPerson.getKey());
		assertTrue(firstPerson.getKey()>0);
		
		assertEquals("Allsun", firstPerson.getFirstName());
		assertEquals("Poundford", firstPerson.getLastName());
		assertEquals("17th Floor", firstPerson.getAddress());
		assertEquals("Female", firstPerson.getGender());
		assertTrue(firstPerson.getEnabled());
		
		PersonDTO sixthPerson = people.get(6);
			
		assertNotNull(sixthPerson.getKey());
		assertTrue(sixthPerson.getKey()>0);
		
		assertEquals("Aloise", sixthPerson.getFirstName());
		assertEquals("Whitecross", sixthPerson.getLastName());
		assertEquals("6th Floor", sixthPerson.getAddress());
		assertEquals("Female", sixthPerson.getGender());
		assertTrue(sixthPerson.getEnabled());
	}
	
	//{{baseUrl}}/api/person/v1/findByName/and?page=0&size=12&direction=asc
	@Test
	@Order(7)
	void findByNameTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("firstName", "and")
				.queryParam("page", 0, "size", 12, "direction", "asc")
				.when()
				.get("findByName/{firstName}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		
		WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
		List<PersonDTO> people = wrapper.getEmbedded().getPeople();
		//List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});
		PersonDTO firstPerson = people.get(0);
		
		assertNotNull(firstPerson.getKey());
		assertTrue(firstPerson.getKey()>0);
		
		assertEquals("Aland", firstPerson.getFirstName());
		assertEquals("Vern", firstPerson.getLastName());
		assertEquals("Room 194", firstPerson.getAddress());
		assertEquals("Male", firstPerson.getGender());
		assertEquals(true, firstPerson.getEnabled());
		
		PersonDTO thirdPerson = people.get(3);
		
		assertNotNull(thirdPerson.getKey());
		assertTrue(thirdPerson.getKey()>0);
		
		assertEquals("Alexandra", thirdPerson.getFirstName());
		assertEquals("Arguile", thirdPerson.getLastName());
		assertEquals("11th Floor", thirdPerson.getAddress());
		assertEquals("Female", thirdPerson.getGender());
		assertTrue(thirdPerson.getEnabled());
	}
	
	private void mockPerson() {
		person.setFirstName("Hanabi");
		person.setLastName("Jumiko");
		person.setAddress("Tokyo-Japan");
		person.setGender("Female");
		person.setEnabled(true);
		
	}
}
