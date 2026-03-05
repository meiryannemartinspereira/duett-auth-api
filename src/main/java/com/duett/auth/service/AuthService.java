package com.duett.auth.service;

import com.duett.auth.dto.AuthRequest;
import com.duett.auth.dto.AuthResponse;
import com.duett.auth.dto.RegisterRequest;
import com.duett.auth.dto.UserResponse;
import com.duett.auth.entity.Role;
import com.duett.auth.entity.User;
import com.duett.auth.exception.UserNotFoundException;
import com.duett.auth.repository.UserRepository;
import com.duett.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(@NonNull RegisterRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        if (repository.existsByCpf(request.cpf())) {
            throw new RuntimeException("CPF already in use");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .cpf(request.cpf())
                .password(passwordEncoder.encode(request.password()))
                .tokenVersion(0)
                .role(Role.USER)
                .build();

        User savedUser = repository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String jwtToken = jwtService.generateAccessToken(userDetails);

        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(@NonNull AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String jwtToken = jwtService.generateAccessToken(userDetails);

        return new AuthResponse(jwtToken);
    }

    public UserResponse getCurrentUser() {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User user = repository.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getRole()
        );
    }
}