package com.EACH.unittest.mockito.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EACH.data.vo.v1.PersonDTO;
import com.EACH.exceptions.RequiredObjectIsNull;
import com.EACH.model.Person;
import com.EACH.repositories.PersonRepository;
import com.EACH.services.PersonServices;
import com.EACH.unittest.mapper.mocker.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	MockPerson input;

	@InjectMocks
	private PersonServices service;

	@Mock
	PersonRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person person = input.mockEntity(1);
		person.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(person));

		var person1 = service.findById(1L);
		assertNotNull(person1);
		assertNotNull(person1.getKey());
		assertNotNull(person1.getLinks());
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("GET")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person/v1")
						&& link.getType().equals("GET")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/api/person/v1")
						&& link.getType().equals("POST")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("PUT")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("DELETE")));
		
		assertThat("Addres Test1").isEqualTo(person1.getAddress());
		assertThat("First Name Test1").isEqualTo(person1.getFirstName());
		assertThat("Last Name Test1").isEqualTo(person1.getLastName());
		assertThat("Female").isEqualTo(person1.getGender());
	}

	@Test
	void testUpdate() {
		Person person = input.mockEntity(1);

		Person persisted = person;
		persisted.setId(1L);

		PersonDTO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(person));

		when(repository.save(person)).thenReturn(persisted);

		var person1 = service.Update(vo, 1L);
		assertNotNull(person1);
		assertNotNull(person1.getKey());
		assertNotNull(person1.getLinks());
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("GET")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person/v1")
						&& link.getType().equals("GET")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/api/person/v1")
						&& link.getType().equals("POST")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("PUT")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("DELETE")));
		assertThat("Addres Test1").isEqualTo(person1.getAddress());
		assertThat("First Name Test1").isEqualTo(person1.getFirstName());
		assertThat("Last Name Test1").isEqualTo(person1.getLastName());
		assertThat("Female").isEqualTo(person1.getGender());
	}

	@Test
	void testCreate() {
		Person person = input.mockEntity(1);
		person.setId(1L);

		Person persisted = person;
		persisted.setId(1L);

		PersonDTO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.save(person)).thenReturn(persisted);

		var person1 = service.create(vo);
		assertNotNull(person1);
		assertNotNull(person1.getKey());
		assertNotNull(person1.getLinks());
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("GET")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person/v1")
						&& link.getType().equals("GET")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/api/person/v1")
						&& link.getType().equals("POST")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("PUT")));
		
		assertNotNull(person1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/api/person/v1/1")
						&& link.getType().equals("DELETE")));
		assertThat("First Name Test1").isEqualTo(person1.getFirstName());
		assertThat("Last Name Test1").isEqualTo(person1.getLastName());
		assertThat("Female").isEqualTo(person1.getGender());
	}

	@Test
	void testCreateWithNullPerson() {

		Exception exception = Assertions.assertThrows(RequiredObjectIsNull.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It's not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testUpdateWithNullPerson() {

		Exception exception = Assertions.assertThrows(RequiredObjectIsNull.class, () -> {
			service.Update(null, null);
		});

		String expectedMessage = "It's not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testDelete() {
		Person person = input.mockEntity(1);
		person.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(person));

		service.delete(1L);
	}

}
