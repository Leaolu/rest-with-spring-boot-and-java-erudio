package com.EACH.integrationtests.DTO.wrapper.xml;

import java.io.Serializable;
import java.util.List;

import com.EACH.integrationtests.DTO.PersonDTO;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson implements Serializable{ 
	private static final long serialVersionUID = 1L;
	

	public List<PersonDTO> content;
	
	public PagedModelPerson() {
	}

	public List<PersonDTO> getContent() {
		return content;
	}

	
	
	
}