package com.EACH.integrationtests.DTO.wrapper.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperPersonDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_embedded")
	private PersonEmbeddedDTO embedded;

	public WrapperPersonDTO() {
	}
	public PersonEmbeddedDTO getEmbedded() {
		return embedded;
	}

	public void setEmbeded(PersonEmbeddedDTO embedded) {
		this.embedded = embedded;
	}
	
	
	
}
 