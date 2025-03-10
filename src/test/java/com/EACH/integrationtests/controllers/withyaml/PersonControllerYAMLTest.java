package com.EACH.integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.EACH.configs.TestConfigs;
import com.EACH.integrationtests.DTO.PersonVO;
import com.EACH.util.MediaType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
public class PersonControllerYAMLTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	private static YAMLMapper yamlMapper;
	
	private static PersonVO person;
	
	
	 @BeforeAll
	    static void setUp() {
	        // Initialize YAMLMapper
	        yamlMapper = new YAMLMapper();
	        yamlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	        // Configure RestAssured to use YAMLMapper for both serialization and deserialization
	        RestAssured.config = RestAssured.config()
	        	    .encoderConfig(EncoderConfig.encoderConfig()
	        	        .encodeContentTypeAs(MediaType.APPLICATION_YML, ContentType.TEXT))
	        	    .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
	        	        .jackson2ObjectMapperFactory((cls, charset) -> yamlMapper));

	        // Initialize the person object
	        person = new PersonVO();
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

	        // Serialize the PersonVO object to YAML
	        String requestBody = yamlMapper.writeValueAsString(person);
	        System.out.println("Request Body (YAML): " + requestBody);

	        var content = given(specification)
	                .contentType(MediaType.APPLICATION_YML)
	                .accept(MediaType.APPLICATION_YML)
	                .body(requestBody) // Send the serialized YAML string
	                .when()
	                .post()
	                .then()
	                .statusCode(201)
	                .contentType(MediaType.APPLICATION_YML)
	                .extract()
	                .body()
	                .asString();

	        System.out.println("Response Body (YAML): " + content);

	        PersonVO createdPerson = yamlMapper.readValue(content, PersonVO.class);

	        person = createdPerson;

	        assertNotNull(createdPerson.getKey());
	        assertTrue(createdPerson.getKey() > 0);

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
		
		String requestBody = yamlMapper.writeValueAsString(person);
        System.out.println("Request Body (YAML): " + requestBody);
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_YML)
				.accept(MediaType.APPLICATION_YML)
				.pathParam("id", person.getKey())
				.body(requestBody)
				.when()
				.put("/{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
		PersonVO createdPerson = yamlMapper.readValue(content, PersonVO.class);
		
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
	void findByIdTest() throws com.fasterxml.jackson.databind.JsonMappingException, com.fasterxml.jackson.core.JsonProcessingException {
		var content = given(specification)
			.contentType(MediaType.APPLICATION_YML)
			.accept(MediaType.APPLICATION_YML)
				.pathParam("id", person.getKey())
			.when()
				.get("{id}")
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YML)
				.extract()
					.body()
						.asString();
		PersonVO 
			createdPerson = yamlMapper.readValue(content, PersonVO.class);
		
		
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
				.accept(MediaType.APPLICATION_YML)
				.pathParam("id", person.getKey())
				.when()
				.patch("{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
		PersonVO createdPerson = yamlMapper.readValue(content, PersonVO.class);
		
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
	void deleteTest() {
		given(specification)
				.pathParam("id", person.getKey())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}
	
	@Test
	@Order(6)
	@Disabled("Under development")
	void findAllTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
			.accept(MediaType.APPLICATION_YML)
			.when()
				.get()
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YML)
				.extract()
					.body()
						.asString();
		
		List<PersonVO> people = yamlMapper.readValue(content, new TypeReference<List<PersonVO>>() {});
		PersonVO firstPerson = people.get(0);
		
		assertNotNull(firstPerson.getKey());
		assertTrue(firstPerson.getKey()>0);
		
		assertEquals("Nancy", firstPerson.getFirstName());
		assertEquals("Leao", firstPerson.getLastName());
		assertEquals("SP", firstPerson.getAddress());
		assertEquals("Female", firstPerson.getGender());
		assertTrue(firstPerson.getEnabled());
		
		PersonVO sixthPerson = people.get(6);
			
		assertNotNull(sixthPerson.getKey());
		assertTrue(sixthPerson.getKey()>0);
		
		assertEquals("Ranny", sixthPerson.getFirstName());
		assertEquals("Andrade", sixthPerson.getLastName());
		assertEquals("SP", sixthPerson.getAddress());
		assertEquals("Female", sixthPerson.getGender());
		assertTrue(sixthPerson.getEnabled());
	}
	
	private void mockPerson() {
		person.setFirstName("Hanabi");
		person.setLastName("Jumiko");
		person.setAddress("Tokyo-Japan");
		person.setGender("Female");
		person.setEnabled(true);
		
	}
}
