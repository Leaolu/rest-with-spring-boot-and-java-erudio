package com.EACH.Mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.EACH.data.vo.v2.PersonDTOV2;
import com.EACH.model.Person;

@Service
public class PersonMapper {

	
	public PersonDTOV2 convertEntityToVO(Person person) {
		PersonDTOV2 vo = new PersonDTOV2();
		vo.setId(person.getId());
		vo.setFirstName(person.getFirstName());
		vo.setLastName(person.getLastName());
		vo.setGender(person.getGender());
		vo.setAddress(person.getAddress());
		vo.setBirthday(new Date());
		return vo;
	}
	
	public Person convertVOtoEntity(PersonDTOV2 person) {
		Person vo = new Person();
		vo.setId(person.getId());
		vo.setFirstName(person.getFirstName());
		vo.setLastName(person.getLastName());
		vo.setGender(person.getGender());
		vo.setAddress(person.getAddress());
		return vo;
	}
}
