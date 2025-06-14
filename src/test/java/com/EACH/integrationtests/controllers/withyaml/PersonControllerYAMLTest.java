package com.EACH.integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.EACH.Security.Util.UserDTO;
import com.EACH.configs.TestConfigs;
import com.EACH.integrationtests.DTO.PersonDTO;
import com.EACH.integrationtests.DTO.wrapper.xml.PagedModelPerson;
import com.EACH.util.MediaType;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	private static YAMLMapper objectMapper;
	
	private static PersonDTO person;
	private static UserDTO user;
	private static String key;
	
	
	 @BeforeAll
	    static void setUp() {
	        // Initialize objectMapper
	        objectMapper = new YAMLMapper();
	        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        // Configure RestAssured to use YAMLMapper for both serialization and deserialization
	        RestAssured.config = RestAssured.config()
	        	    .encoderConfig(EncoderConfig.encoderConfig()
	        	        .encodeContentTypeAs(MediaType.APPLICATION_YML, ContentType.TEXT))
	        	    .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
	        	        .jackson2ObjectMapperFactory((cls, charset) -> objectMapper));

	        // Initialize the person object
	        person = new PersonDTO();
	        user = new UserDTO();
	    }
	 
	 @AfterAll
		static void deleteUser() throws JsonProcessingException, JsonMappingException {
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
		
	 @Test
		@Order(1)
		void getAutho() throws JsonMappingException, JsonProcessingException{
			user.setUserName("Name");
			user.setPassword("OI");
			String requestBody = objectMapper.writeValueAsString(user);
				
				specification = new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
						.setBasePath("/api/auth/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
				
						given(specification)
						.contentType(MediaType.APPLICATION_YML)
						.body(requestBody)
						.post("/signUp")
						.then()
						.statusCode(200);
						
						key = given(specification)
								.contentType(MediaType.APPLICATION_YML)
								.body(requestBody)
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
	                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
	                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, key)
	                .setBasePath("/api/person/v1")
	                .setPort(TestConfigs.SERVER_PORT)
	                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
	                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	                .build();

	        // Serialize the PersonDTO object to YAML
	        String requestBody = objectMapper.writeValueAsString(person);
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

	        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);

	        person = createdPerson;

	        assertNotNull(createdPerson.getId());
	        assertTrue(createdPerson.getId() > 0);

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
		
		String requestBody = objectMapper.writeValueAsString(person);
        System.out.println("Request Body (YAML): " + requestBody);
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_YML)
				.accept(MediaType.APPLICATION_YML)
				.pathParam("id", person.getId())
				.body(requestBody)
				.when()
				.put("/{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Hyuga", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}
	
	
	@Test
	@Order(4)
	void findByIdTest() throws com.fasterxml.jackson.databind.JsonMappingException, com.fasterxml.jackson.core.JsonProcessingException {
		var content = given(specification)
			.contentType(MediaType.APPLICATION_YML)
			.accept(MediaType.APPLICATION_YML)
				.pathParam("id", person.getId())
			.when()
				.get("{id}")
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YML)
				.extract()
					.body()
						.asString();
		PersonDTO 
			createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId()>0);
		
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
				.accept(MediaType.APPLICATION_YML)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId()>0);
		
		assertEquals("Hanabi", createdPerson.getFirstName());
		assertEquals("Hyuga", createdPerson.getLastName());
		assertEquals("Tokyo-Japan", createdPerson.getAddress());
		assertEquals("Female", createdPerson.getGender());
		assertFalse(createdPerson.getEnabled());
	}
	@Test
	@Order(6)
	void deleteTest() {
		given(specification)
				.pathParam("id", person.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}

	@Test
	@Order(7)
	void findByNameTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
				.accept(MediaType.APPLICATION_YML)
				.pathParam("firstName", "and")
				.queryParam("page", 0, "size", 12, "direction", "asc")
				.when()
				.get("/findByName/{firstName}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
		
		
		PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
		List<PersonDTO> people = wrapper.getContent();
		PersonDTO firstPerson = people.get(0);
		
		assertNotNull(firstPerson.getId());
		assertTrue(firstPerson.getId()>0);
		
		assertEquals("Aland", firstPerson.getFirstName());
		assertEquals("Vern", firstPerson.getLastName());
		assertEquals("Room 194", firstPerson.getAddress());
		assertEquals("Male", firstPerson.getGender());
		assertEquals(true, firstPerson.getEnabled());
		
		PersonDTO thirdPerson = people.get(3);
		
		assertNotNull(thirdPerson.getId());
		assertTrue(thirdPerson.getId()>0);
		
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
