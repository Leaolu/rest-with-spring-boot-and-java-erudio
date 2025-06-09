package com.EACH.integrationtests.DTO.wrapper.json;

import java.io.Serializable;
import java.util.List;

import com.EACH.data.vo.v1.BookDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookEmbeddedDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("books")
	private List<BookDTO> books;
	
	public BookEmbeddedDTO() {
	}

	public List<BookDTO> getBooks() {
		return books;
	}
	
	public void setBooks(List<BookDTO> books) {
	    this.books = books;
	}
	
}
