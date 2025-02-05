package com.EACH.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EACH.PersonController;
import com.EACH.Mapper.DozerMapper;
import com.EACH.Mapper.custom.PersonMapper;
import com.EACH.data.vo.v1.PersonVO;
import com.EACH.data.vo.v2.PersonVOV2;
import com.EACH.exceptions.RequiredObjectIsNull;
import com.EACH.exceptions.ResourceNotFoundException;
import com.EACH.model.Person;
import com.EACH.repositories.PersonRepository;

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	PersonMapper mapper;
	
	public PersonVO findById(Long id) {
		logger.info("Finding a person");
		var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
		PersonVO vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public List<PersonVO> findAll(){
		logger.info("Finding all people");
		var people = DozerMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
		people
		.stream()
		.forEach
		(x -> x.add(linkTo(methodOn(PersonController.class).findById(x.getKey())).withSelfRel()));
		return people;
	}
	
	public PersonVO create(PersonVO personVO) {
		if(personVO == null) throw new RequiredObjectIsNull();
		logger.info("Creating a person");
		var person = DozerMapper.parseObject(personVO, Person.class);
		PersonVO vo = DozerMapper.parseObject(personRepository.save(person), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVOV2 createV2(PersonVOV2 personVO) {
		logger.info("Creating a person");
		var person = mapper.convertVOtoEntity(personVO);
		return mapper.convertEntityToVO(person);	
		}
	
	public PersonVO Update(PersonVO person, Long id) {
		if(person == null) throw new RequiredObjectIsNull();
		logger.info("Updating a person");
		PersonVO vo = DozerMapper.parseObject(personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found!")), PersonVO.class);
		updatePerson(person, vo);
		personRepository.save(DozerMapper.parseObject(vo, Person.class));
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	private void updatePerson(PersonVO person, PersonVO entity) {
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
	}

	public void delete(Long id) {
		logger.info("Deleting a person");
		Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
		personRepository.delete(entity);
	}
}
