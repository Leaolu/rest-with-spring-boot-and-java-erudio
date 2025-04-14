package com.EACH.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.EACH.Mapper.DozerMapper;
import com.EACH.Mapper.custom.PersonMapper;
import com.EACH.controllers.PersonController;
import com.EACH.data.vo.v1.PersonDTO;
import com.EACH.data.vo.v2.PersonDTOV2;
import com.EACH.exceptions.BadRequestException;
import com.EACH.exceptions.FileStorageException;
import com.EACH.exceptions.RequiredObjectIsNull;
import com.EACH.exceptions.ResourceNotFoundException;
import com.EACH.file.exporter.contract.FileExporter;
import com.EACH.file.exporter.factory.FileExporterFactory;
import com.EACH.file.importer.contract.FileImporter;
import com.EACH.file.importer.factory.FileImporterFactory;
import com.EACH.model.Person;
import com.EACH.repositories.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonServices {
	
	private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());	
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	FileImporterFactory importer;
	
	@Autowired
	FileExporterFactory exporter;
	
	@Autowired
	PersonMapper mapper;
	
	@Autowired
	PagedResourcesAssembler<PersonDTO> assembler;
	
	public PersonDTO findById(Long id) {
		logger.info("Finding a person");
		var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
		PersonDTO vo = DozerMapper.parseObject(entity, PersonDTO.class);
		addHateoasLinks(vo);
		return vo;
	}
	
	public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
		var people = personRepository.findAll(pageable);
		
		logger.info("Finding all people!");
		
		return addLinks(people, pageable);
	}
	public Resource exportPage(Pageable pageable, String acceptHeader) {
		var people = personRepository.findAll(pageable).map(person -> DozerMapper.parseObject(person, PersonDTO.class)).getContent();
		
		logger.info("Exporting a People page!");
		
		FileExporter exporter;
		try {
			exporter = this.exporter.getExporter(acceptHeader);
			return exporter.exportFile(people);
		} catch (Exception e) {
			throw new BadRequestException("Error during file export!");
		}
	}
	
	public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable){
		
		var people = personRepository.findPeopleByName(firstName, pageable);
		
		logger.info("Finding people by name");
		
		return addLinks(people, pageable);
	}
	
	public PersonDTO create(PersonDTO PersonDTO) {
		if(PersonDTO == null) throw new RequiredObjectIsNull();
		logger.info("Creating a person");
		var person = DozerMapper.parseObject(PersonDTO, Person.class);
		PersonDTO vo = DozerMapper.parseObject(personRepository.save(person), PersonDTO.class);
		addHateoasLinks(vo);
		return vo;
	}
	
	public PersonDTOV2 createV2(PersonDTOV2 PersonDTO) {
		logger.info("Creating a person");
		var person = mapper.convertVOtoEntity(PersonDTO);
		return mapper.convertEntityToVO(person);	
		}
	
	public List<PersonDTO> massCreation(MultipartFile file){
		
		logger.info("Importing people from file");
		
		if(file.isEmpty()) throw new BadRequestException("Please set a valid file!");
		
		try(InputStream inputStream = file.getInputStream()){
			String fileName = Optional.ofNullable(file.getOriginalFilename())
					.orElseThrow(()-> new BadRequestException("File Name cannot be null!"));
			
			
			FileImporter importer = this.importer.getImporter(fileName);
			
			List<Person> entities = importer.importFile(inputStream).stream()
					.map(dto -> personRepository.save(DozerMapper.parseObject(dto, Person.class)))
					.toList();
			
			return entities.stream().map(vo -> {
				var dto = DozerMapper.parseObject(vo, PersonDTO.class);
				addHateoasLinks(dto);
				return dto;
			}).toList();
		}catch(Exception e) {
			throw new FileStorageException("Error processing the file!");
		}
	}
	
	public PersonDTO Update(PersonDTO person, Long id) {
		if(person == null) throw new RequiredObjectIsNull();
		logger.info("Updating a person");
		PersonDTO vo = DozerMapper.parseObject(personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found!")), PersonDTO.class);
		updatePerson(person, vo);
		personRepository.save(DozerMapper.parseObject(vo, Person.class));
		addHateoasLinks(vo);
		return vo;
	}
	
	private void updatePerson(PersonDTO person, PersonDTO entity) {
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
	}
	
	@Transactional
	public PersonDTO disablePerson(Long id) {
		logger.info("Desabling a person");
		personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
		personRepository.disablePerson(id);
		var DTO = DozerMapper.parseObject(personRepository.findById(id).get(), PersonDTO.class);
		addHateoasLinks(DTO);
		return DTO;
	}
	public ResponseEntity<?> delete(Long id) {
		logger.info("Deleting a person");
		Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
		personRepository.delete(entity);
		return ResponseEntity.noContent().build();
	}
	
	public PagedModel<EntityModel<PersonDTO>> addLinks(Page<Person> people, Pageable pageable){
		var peopleWithLinks = people.map(person -> {
			var DTO = DozerMapper.parseObject(person, PersonDTO.class);
			addHateoasLinks(DTO);
			return DTO;
		});
		
		Link findAllLink = 
				WebMvcLinkBuilder.linkTo
				(WebMvcLinkBuilder.methodOn
						(PersonController.class)
						.findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort())))
				.withSelfRel();
		return assembler.toModel(peopleWithLinks, findAllLink);
		
	}
	
	private void addHateoasLinks(PersonDTO DTO) {
		DTO.add(linkTo(methodOn(PersonController.class).findById(DTO.getKey())).withSelfRel().withType("GET"));
		DTO.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
		DTO.add(linkTo(methodOn(PersonController.class).findByName("" ,1, 12, "asc")).withRel("findByName").withType("GET"));
		DTO.add(linkTo(methodOn(PersonController.class).create(DTO)).withRel("create").withType("POST"));
		DTO.add(linkTo(methodOn(PersonController.class)).slash("massCreation").withRel("massCreation").withType("POST"));
		DTO.add(linkTo(methodOn(PersonController.class).disablePerson(DTO.getKey())).withRel("disable").withType("PATCH"));
		DTO.add(linkTo(methodOn(PersonController.class).update(DTO, DTO.getKey())).withRel("update").withType("PUT"));
		DTO.add(linkTo(methodOn(PersonController.class).delete(DTO.getKey())).withRel("delete").withType("DELETE"));
		DTO.add(linkTo(methodOn(PersonController.class).exportPage
				(1, 12, "asc", null)).withRel("exportPage").withType("GET"));
		
	}
}
