package com.EACH.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EACH.BookController;
import com.EACH.Mapper.DozerMapper;
import com.EACH.data.vo.v1.BookDTO;
import com.EACH.exceptions.RequiredObjectIsNull;
import com.EACH.exceptions.ResourceNotFoundException;
import com.EACH.model.Book;
import com.EACH.repositories.BookRepository;

@Service
public class BookServices {
	
	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	@Autowired
	BookRepository Repository;
	
	public BookDTO findById(Long id) {
		logger.info("Finding Book!");
		var entity = Repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book couldn't be found, please insert a valid ID."));
		BookDTO DTO = DozerMapper.parseObject(entity, BookDTO.class);
		DTO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return DTO;
	}
	
	public List<BookDTO> findAll(){
		logger.info("Finding all Books");
		List<BookDTO> books = DozerMapper.parseListObjects(Repository.findAll(), BookDTO.class);
		books.stream().forEach(x -> x.add(linkTo(methodOn(BookController.class).findById(x.getKey())).withSelfRel()));
		return books;
	}
	
	public BookDTO create(BookDTO DTO) {
		if(DTO == null) throw new RequiredObjectIsNull();
		logger.info("Creating a Book");
		var book = DozerMapper.parseObject(DTO, Book.class);
		BookDTO bookDTO = DozerMapper.parseObject(Repository.save(book), BookDTO.class);
		bookDTO.add(linkTo(methodOn(BookController.class).findById(bookDTO.getKey())).withSelfRel());
		return bookDTO;
	}
	
	public BookDTO Update(BookDTO bookDTO, Long id) {
		if(bookDTO  == null) throw new RequiredObjectIsNull();
		logger.info("Updating a Book");
		BookDTO book = findById(id);
		updateBook(bookDTO, book);
		Repository.save(DozerMapper.parseObject(book, Book.class));
		book.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return book;
		
	}
	
	private void updateBook(BookDTO updated, BookDTO original) {
		original.setLaunchDate(updated.getLaunchDate());
		original.setAuthor(updated.getAuthor());
		original.setPrice(updated.getPrice());
		original.setTitle(updated.getTitle());
	}
	
	public void delete(Long id) {
		logger.info("Deleting a Book");
		Book book = Repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not Found!"));
		Repository.delete(book);
	}
}
