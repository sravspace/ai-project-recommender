package com.project.aiprojectrecommender.auth.security;

import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n======================================");
        System.out.println("Incoming Request: " + request.getMethod() + " " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("No Bearer token found. Skipping JWT authentication.");

            filterChain.doFilter(request, response);
            return;

        }

        try {

            String token = authHeader.substring(7);

            System.out.println("JWT Token:");
            System.out.println(token);

            String email = jwtService.extractEmail(token);

            System.out.println("Extracted Email: " + email);

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found in database."));

                boolean valid = jwtService.isTokenValid(token, user);

                System.out.println("Token Valid: " + valid);

                if (valid) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    System.out.println("Authentication stored in SecurityContext.");

                } else {

                    System.out.println("JWT validation failed.");

                }

            }

        } catch (Exception e) {

            System.out.println("JWT Exception Occurred:");
            e.printStackTrace();

        }

        filterChain.doFilter(request, response);

    }

}
