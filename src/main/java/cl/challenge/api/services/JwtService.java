package cl.challenge.api.services;

import org.springframework.security.core.userdetails.UserDetails;

import cl.challenge.api.models.User;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);
}
