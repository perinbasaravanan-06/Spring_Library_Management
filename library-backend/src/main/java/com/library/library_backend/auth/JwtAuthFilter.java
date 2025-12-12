package com.library.library_backend.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserRepository userRepo;

	public JwtAuthFilter(JwtService jwtService, UserRepository userRepo) {
		this.jwtService = jwtService;
		this.userRepo = userRepo;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		final String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			chain.doFilter(req, res);
			return;
		}

		final String token = authHeader.substring(7);
		if (!jwtService.isTokenValid(token)) {
			chain.doFilter(req, res);
			return;
		}

		String username = jwtService.usernameFromToken(token);
		String role = jwtService.roleFromToken(token);

		var authorities = List.of(new SimpleGrantedAuthority(role));
		var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);

		chain.doFilter(req, res);
	}
}