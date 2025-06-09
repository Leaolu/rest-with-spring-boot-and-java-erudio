package com.EACH.Security.Util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.EACH.exceptions.UnAuthoException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtRefreshUtil {
	
	private String secret;
	private int expSec;
	
	private SecretKey key;
	
	@PostConstruct
	public void init() {
		this.secret = "3f229f691decff3d0d20fd1157279618e6cabf670b5026c519344fe88dbf73165b2cfcd8697e62f32d26a1f02834944bbd90e012e4dc35d363439640f7840e9a9fb0626979792d23ccd91807ae0d8af25387c3ebfe5437e0a7d6f9297c7d0c3a269bf4d159329a433642367c4beca212b9cfc61168a21520b4c774cfa62cb809b22b94b394a982dbe7f12274b1a48653c0ab85e02c71663970abdfb5871556bd0d57d28b3f2a80f56627d91b0e341aeeeb8820fbdb618c9d0814db937f073a91a915c3277a6a531812750b331455e97d6daa5967bac6443a26112786c5e5331ddc9ef51e2acaa9621db6027f43c00babcb92b06b8c316b9d3bdb5dbb91a000fc";
		this.expSec = 7200000;
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
