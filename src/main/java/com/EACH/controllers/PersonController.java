package com.EACH.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.EACH.controllers.docs.PersonControllerDocs;
import com.EACH.data.vo.v1.PersonDTO;
import com.EACH.data.vo.v2.PersonDTOV2;
import com.EACH.file.exporter.MediaTypes;
import com.EACH.services.PersonServices;
import com.EACH.util.MediaType;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

// @CrossOrigin(origins = "http://localhost:8080") for CORS at controller level
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoint for Managing People")
public class PersonController implements PersonControllerDocs{

	@Autowired
	private PersonServices personService;

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML })
	public PersonDTO findById(@PathVariable Long id) {
		return personService.findById(id);
	}

	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
			@RequestParam(defaultValue = "0")Integer page,
			@RequestParam(defaultValue = "12")Integer size,
			@RequestParam(defaultValue = "asc")String direction
			){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(personService.findAll(pageable));
	}
	
	@GetMapping(value = "/exportPage", produces = { MediaTypes.APPLICATION_XLSX_VALUE, MediaTypes.APPLICATION_CSV_VALUE})
	public ResponseEntity<Resource> exportPage(
			@RequestParam(defaultValue = "0")Integer page,
			@RequestParam(defaultValue = "12")Integer size,
			@RequestParam(defaultValue = "asc")String direction,
			HttpServletRequest request
			){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		
		String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
		
		Resource file = personService.exportPage(pageable, acceptHeader);
		
		var contentType = (acceptHeader != null)? acceptHeader :"application/ocet-stream" ;
		var fileExtension = MediaTypes.APPLICATION_XLSX_VALUE.equalsIgnoreCase(acceptHeader)? ".xlsx" : ".csv";
		var fileName = "people_exported " + fileExtension;
	
		return ResponseEntity.ok().contentType(org.springframework.http.MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION
						,"attachment; filename=\"" + fileName + "\"")
				.body(file);
	}
	
	@GetMapping(value = "/findByName/{firstName}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
			@PathVariable String firstName,
			@RequestParam(defaultValue = "0")Integer page,
			@RequestParam(defaultValue = "12")Integer size,
			@RequestParam(defaultValue = "asc")String direction
			) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(personService.findByName(firstName ,pageable));
	}

	// @CrossOrigin(origins = {"http://localhost:8080",
	// "https://github.com/Leaolu/rest-with-spring-boot-and-java-erudio"})
	@PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@ResponseStatus(HttpStatus.CREATED)
	public PersonDTO create(@RequestBody PersonDTO person) {
		return personService.create(person);
	}

	@PostMapping(value = "/v2", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@ResponseStatus(HttpStatus.CREATED)
	public PersonDTOV2 createV2(@RequestBody PersonDTOV2 person) {
		return personService.createV2(person);
	}
	
	@PostMapping(value = "/massiveCreation", 
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML })
	public List<PersonDTO> massCreation(@RequestParam MultipartFile file){
		return personService.massCreation(file);
		
	}

	@PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	public PersonDTO update(@RequestBody PersonDTO person, @PathVariable Long id) {
		return personService.Update(person, id);
	}
	
	@PatchMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML })
	@Override
	public PersonDTO disablePerson(Long id) {
		return personService.disablePerson(id);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return personService.delete(id);
	}

}
