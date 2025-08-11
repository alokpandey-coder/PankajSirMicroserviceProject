package com.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.authservice.entity.User;

@RestController
@RequestMapping("/api/v1/auth")
public class WelcomeController {

    @GetMapping("/message")
    public Map<String, String> getMessage(@AuthenticationPrincipal User user) {
        
        if (user == null) {
            return Map.of("message", "Anonymous user");
        }

        return Map.of(
            "name", user.getName(),
            "username", user.getUsername(),
            "email", user.getEmail()
        );
    }
}
