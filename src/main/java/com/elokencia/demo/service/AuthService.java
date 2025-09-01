package com.elokencia.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.elokencia.demo.domain.User;
import com.elokencia.demo.repository.UserRepository;

@Component
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Return an existing local user matching the JWT email or create one.
     * Also persist external id (sub) when available.
     */
    public User getOrCreateUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Microsoft tokens may use "email" or "preferred_username"
    String email = jwt.getClaims().containsKey("email") ? jwt.getClaim("email") : jwt.getClaim("preferred_username");
    String name = jwt.getClaims().containsKey("name") ? jwt.getClaim("name") : null;
    String externalId = jwt.getClaims().containsKey("sub") ? jwt.getClaim("sub") : null;

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name != null ? name : email);
            newUser.setExternalId(externalId);
            return userRepository.save(newUser);
        });

        // Ensure externalId is stored for existing users
        if (externalId != null && (user.getExternalId() == null || !externalId.equals(user.getExternalId()))) {
            user.setExternalId(externalId);
            user = userRepository.save(user);
        }

        return user;
    }

    public User getUserIfExists(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
    String email = jwt.getClaims().containsKey("email") ? jwt.getClaim("email") : jwt.getClaim("preferred_username");
        return userRepository.findByEmail(email).orElse(null);
    }
}
