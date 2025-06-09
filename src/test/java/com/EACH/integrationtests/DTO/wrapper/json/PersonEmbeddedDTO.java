package com.EACH.integrationtests.DTO.wrapper.json;

import java.io.Serializable;
import java.util.List;

import com.EACH.data.vo.v1.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonEmbeddedDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("people")
	private List<PersonDTO> people;
	
	public PersonEmbeddedDTO() {
	}

	public List<PersonDTO> getPeople() {
		return people;
	}
	
	
}
