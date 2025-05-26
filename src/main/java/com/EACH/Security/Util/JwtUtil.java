package com.EACH.Security.Util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.EACH.exceptions.UnAuthoException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration}")
	private int expSec;
	
	private SecretKey key;
	
	@PostConstruct
	public void init() {
		if(secret == null || secret.trim().isEmpty()) {
			throw new IllegalStateException("JWT secret is not configured in application properties");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateToken(String name) {
		return Jwts.builder()
				.subject(name)
				.issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + expSec))
				.signWith(key)
				.compact();
	}
	
	public String getNameFromToken(String token) {
		return  Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	

	public Boolean validateJWTsToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			return true;
		}catch(SecurityException e) {
			throw new UnAuthoException("Invalid JWT signature: "+e.getMessage());
		}
		catch(MalformedJwtException e) {
			throw new UnAuthoException("Invalid JWT token: "+e.getMessage());
		}
		catch(ExpiredJwtException e){
			throw new UnAuthoException("JWT token is expired: "+e.getMessage());
		}
		catch(UnsupportedJwtException e){
			throw new UnAuthoException("JWT token is unsupported: "+e.getMessage());
		}
		catch(IllegalArgumentException e){
			throw new UnAuthoException("JWT claims String is empty: "+e.getMessage());
		}
	}
}
