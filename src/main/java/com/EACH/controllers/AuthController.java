package com.EACH.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EACH.Security.Util.JwtUtil;
import com.EACH.Security.Util.UserDTO;
import com.EACH.Security.Util.UserServices;
import com.EACH.Security.Util.UserUtil;
import com.EACH.controllers.docs.AuthControllerDocs;
import com.EACH.exceptions.BadRequestException;
import com.EACH.exceptions.NameAlreadyExistsException;
import com.EACH.exceptions.UnAuthoException;
import com.EACH.util.MediaType;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController implements AuthControllerDocs{
	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private UserServices service;
	@Autowired
	private JwtUtil jwt;
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	@PostMapping(value = "/signUp", 
			consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}
	)
	public ResponseEntity<String> RegisterUser(@Valid @RequestBody UserDTO user) {
		if(service.userNameExists(user)) {
			throw new NameAlreadyExistsException();
		}
		if(user.getPassword() == null) {
			throw new BadRequestException("Password must not be null!");
		}
		user.setPassword(encoder.encode(user.getPassword()));
		service.save(user);
		return ResponseEntity.ok("User: "+user.getUserName() +" saved!");
		
	}
	
	@PostMapping(value = "/signIn", 
			consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}
	)
	public ResponseEntity<String> authenticateUser(@RequestBody UserDTO user){
		Authentication auth = manager.authenticate(
				new UsernamePasswordAuthenticationToken(
						user.getUserName(), 
						user.getPassword()));
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		return ResponseEntity.ok("Bearer "+jwt.generateToken(userDetails.getUsername()));
	}

	@DeleteMapping(value = "/delete")
	public ResponseEntity<?> DeleteUser(@RequestBody UserDTO user) {
		UserUtil VO = service.getByName(user.getUserName());
		if(!encoder.matches(user.getPassword(), VO.getPassword())) {
			throw new UnAuthoException("Wrong Password!");
		}
		service.delete(user);
		return ResponseEntity.noContent().build();
	}


}
