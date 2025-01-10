package com.EACH.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EACH.Mapper.DozerMapper;
import com.EACH.Mapper.custom.PersonMapper;
import com.EACH.data.vo.v1.PersonVO;
import com.EACH.data.vo.v2.PersonVOV2;
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
		return DozerMapper.parseObject(entity, PersonVO.class);
	}
	
	public List<PersonVO> findAll(){
		logger.info("Finding all people");
		return DozerMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
	}
	
	public PersonVO create(PersonVO personVO) {
		logger.info("Creating a person");
		var person = DozerMapper.parseObject(personVO, Person.class);
		return DozerMapper.parseObject(personRepository.save(person), PersonVO.class);
	}
	
	public PersonVOV2 createV2(PersonVOV2 personVO) {
		logger.info("Creating a person");
		var person = mapper.convertVOtoEntity(personVO);
		return mapper.convertEntityToVO(person);	
		}
	
	public PersonVO Update(PersonVO person, Long id) {
		PersonVO entity = DozerMapper.parseObject(personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!")), PersonVO.class);
		updatePerson(person, entity);
		personRepository.save(DozerMapper.parseObject(entity, Person.class));
		return entity;
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
