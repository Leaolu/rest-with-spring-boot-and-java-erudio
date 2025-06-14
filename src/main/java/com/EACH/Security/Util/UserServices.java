package com.EACH.Security.Util;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.EACH.exceptions.UnAuthoException;

@Service
public class UserServices implements UserDetailsService{
	
	@Autowired
	private UserRepository repository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserUtil user = repository.findByName(username);
		if(user == null) {
			throw new UsernameNotFoundException("This username was not found: " +username);
		}
		return new User(user.getUserName(), user.getPassword(), Collections.emptyList());
	}
	
	public UserUtil getByName(String name) throws UsernameNotFoundException{
		return repository.findByName(name);
	}
	
	public void save(UserDTO user) {
		UserUtil newUser = new UserUtil(user.getUserName(), user.getPassword());
		repository.save(newUser);
	}
	
	public boolean userNameExists(UserDTO user) {
		return repository.UserNameExists(user.getUserName());
	}
	
	public void delete(UserDTO user) {
		UserUtil userVO = repository.findByName(user.getUserName());
		repository.delete(userVO);
		
	}
	
}
