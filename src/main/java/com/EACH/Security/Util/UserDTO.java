package com.EACH.Security.Util;

import java.io.Serializable;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String userName;
	private String password;
	
	public UserDTO() {
	}
	
	
	public UserDTO(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}




	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
		
}
