package com.EACH.Security.Auth;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.EACH.exceptions.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJWT implements AuthenticationEntryPoint{

	@Override
	public void commence(
			HttpServletRequest request, 
			HttpServletResponse response,
			AuthenticationException authException) throws IOException{
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		ExceptionResponse res = new ExceptionResponse(
				new Date(),
				"ERROR: Unauthorized!",
				request.getRequestURI());
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonResponse;
			jsonResponse = mapper.writeValueAsString(res);
			response.getWriter().write(jsonResponse);
		
		
		
	}

	
}
