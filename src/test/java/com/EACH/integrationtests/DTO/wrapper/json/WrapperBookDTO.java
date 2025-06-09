package com.EACH.integrationtests.DTO.wrapper.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperBookDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_embedded")
	private BookEmbeddedDTO embedded;

	public WrapperBookDTO() {
	}

	public BookEmbeddedDTO getEmbedded() {
		return embedded;
	}

	public void setEmbeded(BookEmbeddedDTO embedded) {
		this.embedded = embedded;
	}
	
	
	
}
