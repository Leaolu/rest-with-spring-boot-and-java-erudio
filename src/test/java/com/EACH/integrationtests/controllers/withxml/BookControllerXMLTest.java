package com.EACH.integrationtests.controllers.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

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
import com.EACH.integrationtests.DTO.BookDTOSoap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import IntegrationTest.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerXMLTest extends AbstractIntegrationTest{
	
	
	private static RequestSpecification specification;
	private static XmlMapper xmlMapper;
	
	private static BookDTOSoap book;
	private static UserDTO user;
	private static String key;
	
	@BeforeAll
	static void setUp() {
		xmlMapper = new XmlMapper();
		xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		book = new BookDTOSoap();
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
	void createTest(){
		mockBook();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, key)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content = given(specification)
			.contentType(MediaType.APPLICATION_XML_VALUE)
			.accept(MediaType.APPLICATION_XML_VALUE)
				.body(book)
			.when()
				.post()
			.then()
					.statusCode(201)
					.contentType(MediaType.APPLICATION_XML_VALUE)
				.extract()
					.body()
						.asString();
		
		BookDTOSoap createdBook = new BookDTOSoap();
		try {
			createdBook = xmlMapper.readValue(content, BookDTOSoap.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		book = createdBook;
		System.out.println("Conteúdo XML retornado: " + content);
		System.out.println("Objeto desserializado: " + createdBook);
		assertNotNull(createdBook.getId());
		assertTrue(createdBook.getId()>0);
		
		assertEquals("Lucas", createdBook.getAuthor());
		assertEquals("2.5", Double.toString(createdBook.getPrice()));
		assertEquals("Geografia", createdBook.getTitle());
		assertNotNull(createdBook.getLaunchDate());
	}
	
	@Test
	@Order(3)
	void updateTest() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Historia");
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_XML_VALUE)
				.accept(MediaType.APPLICATION_XML_VALUE)
				.pathParam("id", book.getId())
				.body(book)
				.when()
				.put("/{id}")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_XML_VALUE)
				.extract()
				.body()
				.asString();
		BookDTOSoap createdBook = xmlMapper.readValue(content, BookDTOSoap.class);
		
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
	void findByIdTest() throws JsonMappingException, JsonProcessingException {
		var content = given(specification)
			.contentType(MediaType.APPLICATION_XML_VALUE)
			.accept(MediaType.APPLICATION_XML_VALUE)
				.pathParam("id", book.getId())
			.when()
				.get("{id}")
			.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_XML_VALUE)
				.extract()
					.body()
						.asString();
		BookDTOSoap createdBook = xmlMapper.readValue(content, BookDTOSoap.class);
		
		book = createdBook;
		
		assertNotNull(createdBook.getId());
		assertTrue(createdBook.getId()>0);
		
		assertEquals("Lucas", createdBook.getAuthor());
		assertEquals("2.5", Double.toString(createdBook.getPrice()));
		assertEquals("Historia", createdBook.getTitle());
		assertNotNull(createdBook.getLaunchDate());
	}
	@Test
	@Order(5)
	void deleteTest() {
		given(specification)
				.pathParam("id", book.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}
	
	private void mockBook() {
		book.setAuthor("Lucas");
		book.setPrice(2.50);
		book.setTitle("Geografia");
		book.setLaunchDate(new Date());
	}
}
