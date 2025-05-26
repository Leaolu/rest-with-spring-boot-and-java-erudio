package com.EACH.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.EACH.Security.Auth.AuthEntryPointJWT;
import com.EACH.Security.Auth.AuthTokenFilter;
import com.EACH.Security.Util.UserServices;

@Configuration
public class WebSecurityConfig {
	@Autowired
	UserServices services;
	@Autowired
	private AuthEntryPointJWT unauthoHandler;
	
	@Autowired
	private AuthTokenFilter authTokenFilter;
	
	@Bean
	AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

    @Bean
    PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.csrf(x -> x.disable())
    	.cors(y -> y.disable())
    	.exceptionHandling(z -> z.authenticationEntryPoint(unauthoHandler))
    	.sessionManagement(a -> a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	.authorizeHttpRequests(b -> b.requestMatchers("/api/auth/v1/**").permitAll()
    			.anyRequest()
    			.authenticated());
    	http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    	return http.build();
    }
    
}

