package com.EACH.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.EACH.Security.Util.UserDTO;
import com.EACH.configs.TestConfigs;
import com.EACH.data.vo.v1.BookDTO;
import com.EACH.integrationtests.DTO.wrapper.json.WrapperBookDTO;
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
public class BookControllerJsonTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	
	
	private static ObjectMapper objectMapper;
	
	private static BookDTO Book;
	private static UserDTO user;
	private static String key;
	
	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		Book = new BookDTO();
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
							.asString();
					
					
	}

	@Test
	@Order(2)
	void createTest() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, key)
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content = given(specification)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(Book)
			.when()
				.post()
			.then()
					.statusCode(201)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();
		BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
		
		Book = createdBook;
		
		assertNotNull(createdBook.getKey());
		assertTrue(createdBook.getKey()>0);
		
		assertEquals("Lucas", createdBook.getAuthor());
		assertEquals("2.5", Double.toString(createdBook.getPrice()));
		assertEquals("Geografia", createdBook.getTitle());
		assertNotNull(createdBook.getLaunchDate());
	}
	
	@Test
	@Order(3)
	void updateTest() throws JsonMappingException, JsonProcessingException {
		Book.setTitle("Historia");
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", Book.getKey())
				.body(Book)
				.when()
				.put("/{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
		
		Book = createdBook;
		
		assertNotNull(createdBook.getKey());
		assertTrue(createdBook.getKey()>0);
		
		assertEquals("Lucas", createdBook.getAuthor());
		assertEquals("2.5", Double.toString(createdBook.getPrice()));
		assertEquals("Historia", createdBook.getTitle());
		assertNotNull(createdBook.getLaunchDate());
	}
	
	
	@Test
	@Order(4)
	void findByIdTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", Book.getKey())
			.when()
				.get("/{id}")
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();
		BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
		
		Book = createdBook;
		
		assertNotNull(createdBook.getKey());
		assertTrue(createdBook.getKey()>0);
		
		assertEquals("Lucas", createdBook.getAuthor());
		assertEquals("2.5", Double.toString(createdBook.getPrice()));
		assertEquals("Historia", createdBook.getTitle());
		assertNotNull(createdBook.getLaunchDate());
	}
	@Test
	@Order(5)
	void deleteTest() throws JsonMappingException, JsonProcessingException {
		given(specification)
				.pathParam("id", Book.getKey())
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
			.queryParam("page", 0, "size", 12, "direction", "asc")
			.when()
				.get()
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();
		
		WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
		List<BookDTO> books = wrapper.getEmbedded().getBooks();
		//List<BookDTO> books = objectMapper.readValue(content, new TypeReference<List<BookDTO>>() {});
		BookDTO firstBook = books.get(0);
		
		assertNotNull(firstBook.getKey());
		assertTrue(firstBook.getKey()>0);
		
		assertEquals("Abbey Sanches", firstBook.getAuthor());
		assertEquals("10.3", Double.toString(firstBook.getPrice()));
		assertEquals("Already Dead", firstBook.getTitle());
		assertNotNull(firstBook.getLaunchDate());
		
		BookDTO seventhBook = books.get(7);
			
		assertNotNull(seventhBook.getKey());
		assertTrue(seventhBook.getKey()>0);
		
		assertEquals("Adelaide Swine", seventhBook.getAuthor());
		assertEquals("36.7", Double.toString(seventhBook.getPrice()));
		assertEquals("The Horribly Slow Murderer with the Extremely Inefficient Weapon", seventhBook.getTitle());
		assertNotNull(seventhBook.getLaunchDate());
	}
	
	private void mockBook() {
		Book.setAuthor("Lucas");
		Book.setPrice(2.50);
		Book.setTitle("Geografia");
		Book.setLaunchDate(new Date());
	}
}
