package com.EACH.unittest.mockito.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EACH.data.vo.v1.PersonVO;
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

		var result = service.findById(1L);
		Objects.requireNonNull(result);
		Objects.requireNonNull(result.getKey());
		Objects.requireNonNull(result.getLinks());
		assertThat(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]")).isTrue();
		assertThat("Addres Test1").isEqualTo(result.getAddress());
		assertThat("First Name Test1").isEqualTo(result.getFirstName());
		assertThat("Last Name Test1").isEqualTo(result.getLastName());
		assertThat("Female").isEqualTo(result.getGender());
	}

	@Test
	void testFindAll() {
		List<Person> list = input.mockEntityList();

		when(repository.findAll()).thenReturn(list);
		var people = service.findAll();

		Assertions.assertNotNull(people);
		Assertions.assertEquals(14, people.size());

		var personOne = people.get(1);
		Objects.requireNonNull(personOne);
		Objects.requireNonNull(personOne.getKey());
		Objects.requireNonNull(personOne.getLinks());
		assertThat(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]")).isTrue();
		assertThat("Addres Test1").isEqualTo(personOne.getAddress());
		assertThat("First Name Test1").isEqualTo(personOne.getFirstName());
		assertThat("Last Name Test1").isEqualTo(personOne.getLastName());
		assertThat("Female").isEqualTo(personOne.getGender());

		var personFour = people.get(4);
		Objects.requireNonNull(personFour);
		Objects.requireNonNull(personFour.getKey());
		Objects.requireNonNull(personFour.getLinks());
		assertThat(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]")).isTrue();
		assertThat("Addres Test4").isEqualTo(personFour.getAddress());
		assertThat("First Name Test4").isEqualTo(personFour.getFirstName());
		assertThat("Last Name Test4").isEqualTo(personFour.getLastName());
		assertThat("Male").isEqualTo(personFour.getGender());

		var personSeven = people.get(7);
		Objects.requireNonNull(personSeven);
		Objects.requireNonNull(personSeven.getKey());
		Objects.requireNonNull(personSeven.getLinks());
		assertThat(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]")).isTrue();
		assertThat("Addres Test7").isEqualTo(personSeven.getAddress());
		assertThat("First Name Test7").isEqualTo(personSeven.getFirstName());
		assertThat("Last Name Test7").isEqualTo(personSeven.getLastName());
		assertThat("Female").isEqualTo(personSeven.getGender());

	}

	@Test
	void testUpdate() {
		Person person = input.mockEntity(1);

		Person persisted = person;
		persisted.setId(1L);

		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(person));

		when(repository.save(person)).thenReturn(persisted);

		var result = service.Update(vo, 1L);
		Objects.requireNonNull(result);
		Objects.requireNonNull(result.getKey());
		Objects.requireNonNull(result.getLinks());
		assertThat(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]")).isTrue();
		assertThat("Addres Test1").isEqualTo(result.getAddress());
		assertThat("First Name Test1").isEqualTo(result.getFirstName());
		assertThat("Last Name Test1").isEqualTo(result.getLastName());
		assertThat("Female").isEqualTo(result.getGender());
	}

	@Test
	void testCreate() {
		Person person = input.mockEntity(1);
		person.setId(1L);

		Person persisted = person;
		persisted.setId(1L);

		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(repository.save(person)).thenReturn(persisted);

		var result = service.create(vo);
		Objects.requireNonNull(result);
		Objects.requireNonNull(result.getKey());
		Objects.requireNonNull(result.getLinks());
		assertThat(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]")).isTrue();
		assertThat("Addres Test1").isEqualTo(result.getAddress());
		assertThat("First Name Test1").isEqualTo(result.getFirstName());
		assertThat("Last Name Test1").isEqualTo(result.getLastName());
		assertThat("Female").isEqualTo(result.getGender());
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
