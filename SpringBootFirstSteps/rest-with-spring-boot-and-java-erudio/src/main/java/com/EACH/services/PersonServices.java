package com.EACH.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EACH.exceptions.ResourceNotFoundException;
import com.EACH.model.Person;
import com.EACH.repositories.PersonRepository;

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository personRepository;
	
	public Person findById(Long id) {
		logger.info("Finding a person");
		return personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
	}
	
	public List<Person> findAll(){
		logger.info("Finding all people");
		return personRepository.findAll();
	}
	
	public Person create(Person person) {
		logger.info("Creating a person");
		return personRepository.save(person);
	}
	
	public Person Update(Person person, Long id) {
		Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found!"));
		updatePerson(person, entity);
		return personRepository.save(entity);
	}
	
	private void updatePerson(Person person, Person entity) {
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
