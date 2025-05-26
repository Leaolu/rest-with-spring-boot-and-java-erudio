package com.EACH.Security.Auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.EACH.Security.Util.JwtUtil;
import com.EACH.Security.Util.UserServices;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{

		@Autowired
		private JwtUtil jwtUtil;
		
		@Autowired
		private UserServices services;
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
				String jwt = parseJwt(request);
				if(jwt != null && jwtUtil.validateJWTsToken(jwt)) {
					String name = jwtUtil.getNameFromToken(jwt);
					UserDetails user = services.loadUserByUsername(name);
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(auth);		
				}
				filterChain.doFilter(request, response);
		}
		private String parseJwt(HttpServletRequest request) {
			String headerAuth = request.getHeader("Authorization");
			if(headerAuth != null && headerAuth.startsWith("Bearer ")) {
				return headerAuth.substring(7);
			}
			return null;
		}
}
