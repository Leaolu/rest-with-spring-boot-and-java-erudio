package com.EACH.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EACH.Mapper.DozerMapper;
import com.EACH.controllers.BookController;
import com.EACH.data.vo.v1.BookDTO;
import com.EACH.exceptions.RequiredObjectIsNull;
import com.EACH.exceptions.ResourceNotFoundException;
import com.EACH.model.Book;
import com.EACH.repositories.BookRepository;

@Service
public class BookServices {
	
	private Logger logger = LoggerFactory.getLogger(BookServices.class.getName());
	
	@Autowired
	BookRepository Repository;
	
	public BookDTO findById(Long id) {
		logger.info("Finding Book!");
		var entity = Repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book couldn't be found, please insert a valid ID."));
		BookDTO DTO = DozerMapper.parseObject(entity, BookDTO.class);
		addHateoasLinks(DTO);
		return DTO;
	}
	
	public List<BookDTO> findAll(){
		logger.info("Finding all Books");
		List<BookDTO> books = DozerMapper.parseListObjects(Repository.findAll(), BookDTO.class);
		books.stream().forEach(x ->addHateoasLinks(x));
		return books;
	}
	
	public BookDTO create(BookDTO DTO) {
		if(DTO == null) throw new RequiredObjectIsNull();
		logger.info("Creating a Book");
		var book = DozerMapper.parseObject(DTO, Book.class);
		BookDTO bookDTO = DozerMapper.parseObject(Repository.save(book), BookDTO.class);
		addHateoasLinks(bookDTO);
		return bookDTO;
	}
	
	public BookDTO Update(BookDTO bookDTO, Long id) {
		if(bookDTO  == null) throw new RequiredObjectIsNull();
		logger.info("Updating a Book");
		BookDTO book = findById(id);
		updateBook(bookDTO, book);
		Repository.save(DozerMapper.parseObject(book, Book.class));
		addHateoasLinks(book);
		return book;
		
	}
	
	private void updateBook(BookDTO updated, BookDTO original) {
		original.setLaunchDate(updated.getLaunchDate());
		original.setAuthor(updated.getAuthor());
		original.setPrice(updated.getPrice());
		original.setTitle(updated.getTitle());
	}
	
	public ResponseEntity<?> delete(Long id) {
		logger.info("Deleting a Book");
		Book book = Repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not Found!"));
		Repository.delete(book);
		return ResponseEntity.noContent().build();
	}
	
	private void addHateoasLinks(BookDTO DTO) {
		DTO.add(linkTo(methodOn(BookController.class).findById(DTO.getKey())).withSelfRel().withType("GET"));
		DTO.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
		DTO.add(linkTo(methodOn(BookController.class).create(DTO)).withRel("create").withType("POST"));
		DTO.add(linkTo(methodOn(BookController.class).update(DTO, DTO.getKey())).withRel("update").withType("PUT"));
		DTO.add(linkTo(methodOn(BookController.class).delete(DTO.getKey())).withRel("delete").withType("DELETE"));
		
	}
}
