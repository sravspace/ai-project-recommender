package com.project.aiprojectrecommender.auth.service;

import com.project.aiprojectrecommender.auth.dto.AuthResponse;
import com.project.aiprojectrecommender.auth.dto.LoginRequest;
import com.project.aiprojectrecommender.auth.dto.RegisterRequest;
import com.project.aiprojectrecommender.auth.security.JwtService;
import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.entity.UserProfile;
import com.project.aiprojectrecommender.enums.Role;
import com.project.aiprojectrecommender.repository.UserProfileRepository;
import com.project.aiprojectrecommender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .user(user)
                .fullName(request.getFullName())
                .profileCompleted(false)
                .build();

        userProfileRepository.save(profile);
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}