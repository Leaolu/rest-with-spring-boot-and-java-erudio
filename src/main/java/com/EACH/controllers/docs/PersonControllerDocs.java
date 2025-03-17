package com.EACH.controllers.docs;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.EACH.data.vo.v1.PersonDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface PersonControllerDocs {
	
	@Operation(summary = "Finds a Person", description = "Finds a Person", tags = { "People" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public PersonDTO findById(@PathVariable Long id);
	
	
	@Operation(summary = "Finds every Person", description = "Finds every Person", tags = { "People" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))) }),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
			@RequestParam(defaultValue = "0")Integer page,
			@RequestParam(defaultValue = "12")Integer size,
			@RequestParam(defaultValue = "asc")String direction
			);
	
	@Operation(summary = "Finds People by First Name", description = "Finds People by their first name", tags = { "People" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))) }),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
			@PathVariable String firstName,
			@RequestParam(defaultValue = "0")Integer page,
			@RequestParam(defaultValue = "12")Integer size,
			@RequestParam(defaultValue = "asc")String direction
			);
	
	
		@Operation(summary = "Adds a New Person", description = "Adds a New Person by passing in a JSON, XML or YML representation of a Person", tags = {
				"People" }, responses = {
						@ApiResponse(description = "Success Creating Person", responseCode = "202", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
						@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
						@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
						@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
		public PersonDTO create(@RequestBody PersonDTO person);
		
		
		@Operation(summary = "Updates a Person", description = "Updates a Person by passing in a JSON, XML or YML representation of a Person", tags = {
		"People" }, responses = {
				@ApiResponse(description = "Updated Successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
		public PersonDTO update(@RequestBody PersonDTO person, @PathVariable Long id);
		
		@Operation(summary = "Disable a Person", description = "Disable a specific Person by their ID", tags = { "People" }, responses = {
				@ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
		public PersonDTO disablePerson(@PathVariable Long id);
		
		@Operation(summary = "Deletes a Person", description = "Deletes a Person", tags = { "People" }, responses = {
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
		public ResponseEntity<?> delete(@PathVariable Long id);
}
