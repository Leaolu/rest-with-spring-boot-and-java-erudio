package com.EACH.integrationtests.repositories;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.EACH.model.Person;
import com.EACH.repositories.PersonRepository;

import IntegrationTest.testcontainers.AbstractIntegrationTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{
	
	@Autowired
	PersonRepository repository;
	
	private static Person person;

	@BeforeAll
	static void setUp() {
		person = new Person();
	}
	
	
	@Test
	@Order(1)
	void findPeopleByName() {
		Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));
		
		person = repository.findPeopleByName("gaby", pageable).getContent().get(0);
		assertNotNull(person);
		assertNotNull(person.getId());
		assertEquals("RJ", person.getAddress());
		assertEquals("Queiroz", person.getLastName());
		assertEquals("Gaby", person.getFirstName());
		assertEquals("Female", person.getGender());
		assertTrue(person.getEnabled());
	}
	
	@Test
	@Order(2)
	void disablePerson() {
		Long id = person.getId();
		repository.disablePerson(id);
		
		var result = repository.findById(id);
		person = result.get();
		
		assertNotNull(person);
		assertNotNull(person.getId());
		assertEquals("RJ", person.getAddress());
		assertEquals("Queiroz", person.getLastName());
		assertEquals("Gaby", person.getFirstName());
		assertEquals("Female", person.getGender());
		assertFalse(person.getEnabled());
	}
}
