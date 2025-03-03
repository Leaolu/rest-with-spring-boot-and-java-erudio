package com.EACH.unittest.mockito.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
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

import com.EACH.data.vo.v1.BookDTO;
import com.EACH.exceptions.RequiredObjectIsNull;
import com.EACH.model.Book;
import com.EACH.repositories.BookRepository;
import com.EACH.services.BookServices;
import com.EACH.unittest.mapper.mocker.MockBook;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;

	@InjectMocks
	private BookServices service;

	@Mock
	BookRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book book = input.mockEntity(1);
		book.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(book));

		var book1 = service.findById(1L);
		Assertions.assertNotNull(book1);
		Assertions.assertNotNull(book1.getKey());
		Assertions.assertNotNull(book1.getLinks());
		
		
		assertNotNull(book1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/api/book/v1/1")
						&& link.getType().equals("GET")));
		
		assertNotNull(book1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("GET")));
		
		assertNotNull(book1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("POST")));
		
		assertNotNull(book1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/api/book/v1/1")
						&& link.getType().equals("PUT")));
		
		assertNotNull(book1.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/api/book/v1/1")
						&& link.getType().equals("DELETE")));
		
		assertThat("Title test1").isEqualTo(book1.getTitle());
		assertThat("Author test1").isEqualTo(book1.getAuthor());
		Assertions.assertNotNull(book1.getLaunchDate());
		assertThat(1D).isEqualTo(book1.getPrice());
	}

	@Test
	void testFindAll() {
		List<Book> list = input.mockEntityList();

		when(repository.findAll()).thenReturn(list);
		var Books = service.findAll();

		Assertions.assertNotNull(Books);
		Assertions.assertEquals(14, Books.size());

		var bookOne = Books.get(1);
		Assertions.assertNotNull(bookOne);
		Assertions.assertNotNull(bookOne.getKey());
		Assertions.assertNotNull(bookOne.getLinks());
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/api/book/v1/1")
						&& link.getType().equals("GET")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("GET")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("POST")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/api/book/v1/1")
						&& link.getType().equals("PUT")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/api/book/v1/1")
						&& link.getType().equals("DELETE")));
		
		assertThat("Title test1").isEqualTo(bookOne.getTitle());
		assertThat("Author test1").isEqualTo(bookOne.getAuthor());
		Assertions.assertNotNull(bookOne.getLaunchDate());
		assertThat(1D).isEqualTo(bookOne.getPrice());

		var bookFour = Books.get(4);
		Assertions.assertNotNull(bookFour);
		Assertions.assertNotNull(bookFour.getKey());
		Assertions.assertNotNull(bookFour.getLinks());
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/api/book/v1/4")
						&& link.getType().equals("GET")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("GET")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("POST")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/api/book/v1/4")
						&& link.getType().equals("PUT")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/api/book/v1/4")
						&& link.getType().equals("DELETE")));
		
		assertThat("Title test4").isEqualTo(bookFour.getTitle());
		assertThat("Author test4").isEqualTo(bookFour.getAuthor());
		Assertions.assertNotNull(bookFour.getLaunchDate());
		assertThat(1D).isEqualTo(bookFour.getPrice());

		var bookSeven = Books.get(7);
		Assertions.assertNotNull(bookSeven);
		Assertions.assertNotNull(bookSeven.getKey());
		Assertions.assertNotNull(bookSeven.getLinks());
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/api/book/v1/7")
						&& link.getType().equals("GET")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("GET")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/api/book/v1")
						&& link.getType().equals("POST")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/api/book/v1/7")
						&& link.getType().equals("PUT")));
		
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/api/book/v1/7")
						&& link.getType().equals("DELETE")));
		
		assertThat("Title test7").isEqualTo(bookSeven.getTitle());
		assertThat("Author test7").isEqualTo(bookSeven.getAuthor());
		Assertions.assertNotNull(bookSeven.getLaunchDate());
		assertThat(1D).isEqualTo(bookSeven.getPrice());

	}

	@Test
	void testUpdate() {
		Book book = input.mockEntity(1);

		Book persisted = book;
		persisted.setId(1L);

		BookDTO DTO = input.mockDTO(1);
		DTO.setKey(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(book));

		when(repository.save(book)).thenReturn(persisted);

		var book1 = service.Update(DTO, 1L);
		Assertions.assertNotNull(book1);
		Assertions.assertNotNull(book1.getKey());
		Assertions.assertNotNull(book1.getLinks());
		assertThat("Title test1").isEqualTo(book1.getTitle());
		assertThat("Author test1").isEqualTo(book1.getAuthor());
		Assertions.assertNotNull(book1.getLaunchDate());
		assertThat(1D).isEqualTo(book1.getPrice());
		assertThat(book1.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
	}

	@Test
	void testCreate() {
		Book book = input.mockEntity(1);
		book.setId(1L);

		Book persisted = book;
		persisted.setId(1L);

		BookDTO DTO = input.mockDTO(1);
		DTO.setKey(1L);

		when(repository.save(book)).thenReturn(persisted);

		var book1 = service.create(DTO);
		Assertions.assertNotNull(book1);
		Assertions.assertNotNull(book1.getKey());
		Assertions.assertNotNull(book1.getLinks());
		assertThat("Title test1").isEqualTo(book1.getTitle());
		assertThat("Author test1").isEqualTo(book1.getAuthor());
		Assertions.assertNotNull(book1.getLaunchDate());
		assertThat(1D).isEqualTo(book1.getPrice());
	}

	@Test
	void testCreateWithNullbook() {

		Exception exception = Assertions.assertThrows(RequiredObjectIsNull.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It's not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testUpdateWithNullbook() {

		Exception exception = Assertions.assertThrows(RequiredObjectIsNull.class, () -> {
			service.Update(null, null);
		});

		String expectedMessage = "It's not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testDelete() {
		Book book = input.mockEntity(1);
		book.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(book));

		service.delete(1L);
	}

}
