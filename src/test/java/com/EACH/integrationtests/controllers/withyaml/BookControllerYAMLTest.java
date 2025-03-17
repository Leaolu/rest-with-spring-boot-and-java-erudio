package com.EACH.integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.EACH.configs.TestConfigs;
import com.EACH.integrationtests.DTO.BookDTOSoap;
import com.EACH.integrationtests.DTO.wrapper.xml.PagedModelBook;
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
public class BookControllerYAMLTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	private static YAMLMapper objectMapper;
	
	private static BookDTOSoap book;
	
	
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

	        // Initialize the book object
	        book = new BookDTOSoap();
	    }

	    @Test
	    @Order(1)
	    void createTest() throws JsonMappingException, JsonProcessingException {
	        mockBook();

	        specification = new RequestSpecBuilder()
	                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
	                .setBasePath("/api/book/v1")
	                .setPort(TestConfigs.SERVER_PORT)
	                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
	                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	                .build();

	        String requestBody = objectMapper.writeValueAsString(book);

	        var content = given(specification)
	                .contentType(MediaType.APPLICATION_YML)
	                .accept(MediaType.APPLICATION_YML)
	                .body(requestBody) 
	                .when()
	                .post()
	                .then()
	                .statusCode(201)
	                .contentType(MediaType.APPLICATION_YML)
	                .extract()
	                .body()
	                .asString();

	        System.out.println("Response Body (YAML): " + content);

	        BookDTOSoap createdBook = objectMapper.readValue(content, BookDTOSoap.class);

	        book = createdBook;

	        assertNotNull(createdBook.getId());
	        assertTrue(createdBook.getId() > 0);

	        assertEquals("Lucas", createdBook.getAuthor());
			assertEquals("2.5", Double.toString(createdBook.getPrice()));
			assertEquals("Geografia", createdBook.getTitle());
			assertNotNull(createdBook.getLaunchDate());
	    }
	
	@Test
	@Order(2)
	void updateTest() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Historia");
		
		String requestBody = objectMapper.writeValueAsString(book);
        System.out.println("Request Body (YAML): " + requestBody);
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_YML)
				.accept(MediaType.APPLICATION_YML)
				.pathParam("id", book.getId())
				.body(requestBody)
				.when()
				.put("/{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_YML)
				.extract()
				.body()
				.asString();
		BookDTOSoap createdBook = objectMapper.readValue(content, BookDTOSoap.class);
		
		book = createdBook;
		
		assertNotNull(createdBook.getId());
		assertTrue(createdBook.getId()>0);
		
		assertEquals("Lucas", createdBook.getAuthor());
		assertEquals("2.5", Double.toString(createdBook.getPrice()));
		assertEquals("Historia", createdBook.getTitle());
		assertNotNull(createdBook.getLaunchDate());
	}
	
	
	@Test
	@Order(3)
	void findByIdTest() throws com.fasterxml.jackson.databind.JsonMappingException, com.fasterxml.jackson.core.JsonProcessingException {
		var content = given(specification)
			.contentType(MediaType.APPLICATION_YML)
			.accept(MediaType.APPLICATION_YML)
				.pathParam("id", book.getId())
			.when()
				.get("{id}")
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YML)
				.extract()
					.body()
						.asString();
		BookDTOSoap 
			createdBook = objectMapper.readValue(content, BookDTOSoap.class);
		
		
		book = createdBook;
		
		assertNotNull(createdBook.getId());
		assertTrue(createdBook.getId()>0);
		
		assertEquals("Lucas", createdBook.getAuthor());
		assertEquals("2.5", Double.toString(createdBook.getPrice()));
		assertEquals("Historia", createdBook.getTitle());
		assertNotNull(createdBook.getLaunchDate());
	}
	@Test
	@Order(4)
	void deleteTest() {
		given(specification)
				.pathParam("id", book.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}
	
	@Test
	@Order(5)
	void findAllTest() throws JsonMappingException, JsonProcessingException {
		 var responseBody = given(specification)
	                .accept(MediaType.APPLICATION_YML)
	                .queryParams("page", 0, "size", 12, "direction", "asc")
	                .when()
	                .get()
	                .then()
	                .statusCode(200)
	                .contentType(MediaType.APPLICATION_YML)
	                .extract()
	                .body()
	                .asString();
		
		PagedModelBook response = objectMapper.readValue(responseBody, PagedModelBook.class);
		List<BookDTOSoap> books = response.getContent();
		BookDTOSoap firstBook = books.get(0);
		
		assertNotNull(firstBook.getId());
		assertTrue(firstBook.getId()>0);
		
		assertEquals("Abbey Sanches", firstBook.getAuthor());
		assertEquals("10.3", Double.toString(firstBook.getPrice()));
		assertEquals("Already Dead", firstBook.getTitle());
		assertNotNull(firstBook.getLaunchDate());
		
		BookDTOSoap seventhBook = books.get(7);
			
		assertNotNull(seventhBook.getId());
		assertTrue(seventhBook.getId()>0);
		
		assertEquals("Adelaide Swine", seventhBook.getAuthor());
		assertEquals("36.7", Double.toString(seventhBook.getPrice()));
		assertEquals("The Horribly Slow Murderer with the Extremely Inefficient Weapon", seventhBook.getTitle());
		assertNotNull(seventhBook.getLaunchDate());
	}
	
	private void mockBook() {
		book.setAuthor("Lucas");
		book.setPrice(2.50);
		book.setTitle("Geografia");
		book.setLaunchDate(new Date());
	}
}
