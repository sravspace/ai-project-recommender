package com.project.aiprojectrecommender.auth.controller;

import com.project.aiprojectrecommender.auth.dto.AuthResponse;
import com.project.aiprojectrecommender.auth.dto.LoginRequest;
import com.project.aiprojectrecommender.auth.dto.RegisterRequest;
import com.project.aiprojectrecommender.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequest request) {

        authService.register(request);

    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        return authService.login(request);

    }

}