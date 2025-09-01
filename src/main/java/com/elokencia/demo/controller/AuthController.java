package com.elokencia.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.elokencia.demo.domain.User;
import com.elokencia.demo.dto.UserDto;
import com.elokencia.demo.service.AuthService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class AuthController {


    private final AuthService currentUserService;
    
    public AuthController(AuthService currentUserService) {
        this.currentUserService = currentUserService;
    }


    @GetMapping("/users/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        User user = currentUserService.getOrCreateUser(authentication);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getEmail()));
    }
}
