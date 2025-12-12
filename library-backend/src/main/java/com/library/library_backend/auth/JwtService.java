package com.library.library_backend.auth;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	// in production store in env var
	private final Key key = Keys
			.hmacShaKeyFor("replace_this_with_long_random_secret_key_which_is_at_least_256_bits".getBytes());
	private final long expirationMs = 1000L * 60 * 60 * 24; // 24 hours

	public String generateToken(User user) {
		return Jwts.builder().setSubject(user.getUsername()).claim("role", user.getRole().name())
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key).compact();
	}

	public Jws<Claims> parseToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	public boolean isTokenValid(String token) {
		try {
			parseToken(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	public String usernameFromToken(String token) {
		return parseToken(token).getBody().getSubject();
	}

	public String roleFromToken(String token) {
		return (String) parseToken(token).getBody().get("role");
	}
}