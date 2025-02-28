package com.EACH.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.EACH.data.vo.v1.PersonVO;
import com.EACH.data.vo.v2.PersonVOV2;
import com.EACH.services.PersonServices;
import com.EACH.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// @CrossOrigin(origins = "http://localhost:8080") for CORS at controller level
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoint for Managing People")
public class PersonController {

	@Autowired
	private PersonServices personService;

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML })
	@Operation(summary = "Finds a Person", description = "Finds a Person", tags = { "People" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public PersonVO findById(@PathVariable Long id) {
		return personService.findById(id);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Finds every Person", description = "Finds every Person", tags = { "People" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))) }),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public List<PersonVO> findAll() {
		return personService.findAll();
	}

	// @CrossOrigin(origins = {"http://localhost:8080",
	// "https://github.com/Leaolu/rest-with-spring-boot-and-java-erudio"})
	@PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Adds a New Person", description = "Adds a New Person by passing in a JSON, XML or YML representation of a Person", tags = {
			"People" }, responses = {
					@ApiResponse(description = "Success Creating Person", responseCode = "202", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonVO.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public PersonVO create(@RequestBody PersonVO person) {
		return personService.create(person);
	}

	@PostMapping(value = "/v2", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@ResponseStatus(HttpStatus.CREATED)
	public PersonVOV2 createV2(@RequestBody PersonVOV2 person) {
		return personService.createV2(person);
	}

	@PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@Operation(summary = "Updates a Person", description = "Updates a Person by passing in a JSON, XML or YML representation of a Person", tags = {
			"People" }, responses = {
					@ApiResponse(description = "Updated Successfully", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonVO.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public PersonVO update(@RequestBody PersonVO person, @PathVariable Long id) {
		return personService.Update(person, id);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Deletes a Person", description = "Deletes a Person", tags = { "People" }, responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content) })
	public void delete(@PathVariable Long id) {
		personService.delete(id);
	}
}
