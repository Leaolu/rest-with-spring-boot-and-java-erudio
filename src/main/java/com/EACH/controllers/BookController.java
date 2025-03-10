package com.EACH.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.EACH.controllers.docs.BookControllerDocs;
import com.EACH.data.vo.v1.BookDTO;
import com.EACH.services.BookServices;
import com.EACH.util.MediaType;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoint for Managing Books")
public class BookController implements BookControllerDocs{
	
	@Autowired
	BookServices services;
	
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	public BookDTO findById(@PathVariable Long id) {
		return services.findById(id);
		
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "12") Integer size,
			@RequestParam(defaultValue = "asc") String direction
			){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "author"));
			return ResponseEntity.ok().body(services.findAll(pageable));	
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO DTO) {
		return services.create(DTO);
	}
	
	@PutMapping(value = "/{id}",consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	public BookDTO update(@RequestBody BookDTO DTO, @PathVariable Long id) {
		return services.Update(DTO, id);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(Long id) {
		return services.delete(id);
	}
}
