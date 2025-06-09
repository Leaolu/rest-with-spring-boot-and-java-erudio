package com.EACH.controllers.docs;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.EACH.data.vo.v1.BookDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface BookControllerDocs {
	
	@Operation(summary = "Finds a Book", description = "Finds a Book", tags = { "Books" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public BookDTO findById(@PathVariable Long id);
	
	
	@Operation(summary = "Finds every Book", description = "Finds every Book", tags = { "Books" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))) }),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "12") Integer size,
			@RequestParam(defaultValue = "asc") String direction
			);
	
	
		@Operation(summary = "Adds a New Book", description = "Adds a New Book by passing in a JSON, XML or YML representation of a Book", tags = {
				"Books" }, responses = {
						@ApiResponse(description = "Success Creating Book", responseCode = "202", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
						@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
						@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
						@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
		public BookDTO create(@RequestBody BookDTO Book);
		
		
		@Operation(summary = "Updates a Book", description = "Updates a Book by passing in a JSON, XML or YML representation of a Book", tags = {
		"Books" }, responses = {
				@ApiResponse(description = "Updated Successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
		public BookDTO update(@RequestBody BookDTO Book, @PathVariable Long id);
		
		@Operation(summary = "Deletes a Book", description = "Deletes a Book", tags = { "Books" }, responses = {
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
		public ResponseEntity<?> delete(@PathVariable Long id);
}
