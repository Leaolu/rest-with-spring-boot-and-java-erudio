package com.EACH.Mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.EACH.data.vo.v2.PersonVOV2;
import com.EACH.model.Person;

@Service
public class PersonMapper {

	
	public PersonVOV2 convertEntityToVO(Person person) {
		PersonVOV2 vo = new PersonVOV2();
		vo.setId(person.getId());
		vo.setFirstName(person.getFirstName());
		vo.setLastName(person.getLastName());
		vo.setGender(person.getGender());
		vo.setAddress(person.getAddress());
		vo.setBirthday(new Date());
		return vo;
	}
	
	public Person convertVOtoEntity(PersonVOV2 person) {
		Person vo = new Person();
		vo.setId(person.getId());
		vo.setFirstName(person.getFirstName());
		vo.setLastName(person.getLastName());
		vo.setGender(person.getGender());
		vo.setAddress(person.getAddress());
		return vo;
	}
}
