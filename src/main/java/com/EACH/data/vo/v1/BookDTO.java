package com.EACH.data.vo.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

@JsonPropertyOrder({"id", "author", "title", "launchDate", "price"})
@Relation(collectionRelation = "books")
public class BookDTO extends RepresentationModel<BookDTO> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Mapping("id")
	@JsonProperty("id")
	private Long key;
	
	private String author;
	
	private Date launchDate;
	
	private String title;
	
	private Double price;
	
	
	public BookDTO() {
	}


	public BookDTO(String author, Date launchDate, String title, Double price) {
		super();
		this.author = author;
		this.launchDate = launchDate;
		this.title = title;
		this.price = price;
	}

	public Long getKey() {
		return key;
	}
 
	public void setKey(Long Key) {
		this.key = Key;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public Date getLaunchDate() {
		return launchDate;
	}


	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(key);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookDTO other = (BookDTO) obj;
		return Objects.equals(key, other.key);
	}


	@Override
	public String toString() {
		return "BookDTO [id=" + key + ", author=" + author + ", launchDate=" + launchDate + ", title=" + title
				+ ", price=" + price + "]";
	}
	
	
}
