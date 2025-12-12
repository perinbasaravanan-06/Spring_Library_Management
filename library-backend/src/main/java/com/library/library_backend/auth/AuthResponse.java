package com.library.library_backend.auth;

public record AuthResponse(String token, String username, String role) {
}
