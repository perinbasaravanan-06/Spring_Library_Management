package com.library.library_backend.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody AuthRequest req) {
		if (userRepo.existsByUsername(req.username())) {
			return ResponseEntity.badRequest().body("Username already exists");
		}
		// first registered user -> become ADMIN (optional); else USER
		Role role = userRepo.count() == 0 ? Role.ROLE_ADMIN : Role.ROLE_USER;

		User user = new User(req.username(), passwordEncoder.encode(req.password()), role);
		userRepo.save(user);
		String token = jwtService.generateToken(user);
		return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest req) {
		return userRepo.findByUsername(req.username()).map(user -> {
			if (passwordEncoder.matches(req.password(), user.getPassword())) {
				String token = jwtService.generateToken(user);
				return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
			} else {
				return ResponseEntity.status(401).body("Invalid credentials");
			}
		}).orElse(ResponseEntity.status(401).body("Invalid credentials"));
	}
}