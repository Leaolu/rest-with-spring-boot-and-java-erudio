package com.EACH.integrationtests.DTO.wrapper.xml;

import java.io.Serializable;
import java.util.List;

import com.EACH.integrationtests.DTO.BookDTOSoap;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook implements Serializable{ 
	private static final long serialVersionUID = 1L;
	

	public List<BookDTOSoap> content;
	
	public PagedModelBook() {
	}

	public List<BookDTOSoap> getContent() {
		return content;
	}

	
	
	
}