package com.EACH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.EACH.data.vo.v1.BookDTO;
import com.EACH.services.BookServices;
import com.EACH.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoint for Managing Books")
public class BookController {
	
	@Autowired
	BookServices services;
	
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds a Book", description = "Finds a Book",
	tags = {"Books"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = 
							@Content(mediaType = "application/json",
									 schema = @Schema(implementation = BookDTO.class)
							)
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
			}
	)
	public BookDTO findById(@PathVariable Long id) {
		return services.findById(id);
		
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds every Book", description = "Finds every Book",
	tags = {"Books"},
	responses = {
			@ApiResponse(description = "Success", responseCode = "200", 
					content = {
							@Content(mediaType = "application/json",
									 array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
							)
					}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
			}
	)
	public List<BookDTO> findAll(){
		return services.findAll();
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Adds a New Book", 
	description = "Adds a New Book by passing in a JSON, XML or YML representation of a Book",
	tags = {"Books"},
	responses = {
			@ApiResponse(description = "Success Creating Book", responseCode = "202", 
					content = 
							@Content(mediaType = "application/json",
									 schema = @Schema(implementation = BookDTO.class)
							)
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
			}
	)
	public BookDTO create(@RequestBody BookDTO DTO) {
		return services.create(DTO);
	}
	
	@PutMapping(value = "/{id}",consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a Book", description = "Updates a Book by passing in a JSON, XML or YML representation of a Book", tags = {
	"Books" }, responses = {
			@ApiResponse(description = "Updated Successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public BookDTO update(@RequestBody BookDTO DTO, @PathVariable Long id) {
		return services.Update(DTO, id);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Deletes a Book", description = "Deletes a Book",
	tags = {"Books"},
	responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
			}
	)
	public void Delete(@PathVariable Long id) {
		services.delete(id);
	}
}
