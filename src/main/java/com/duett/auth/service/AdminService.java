package com.duett.auth.service;

import com.duett.auth.dto.RegisterRequest;
import com.duett.auth.dto.UserResponse;
import com.duett.auth.entity.Role;
import com.duett.auth.entity.User;
import com.duett.auth.exception.UserNotFoundException;
import com.duett.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> listUsers() {

        return repository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCpf(),
                        user.getRole()
                ))
                .toList();
    }

    public void deleteUser(String id) {

        User user = repository.findById(java.util.UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException(id));

        repository.delete(user);
    }

    public UserResponse createAdmin(RegisterRequest request) {

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
                .role(Role.ADMIN)
                .tokenVersion(0)
                .build();

        User saved = repository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getCpf(),
                saved.getRole()
        );
    }
}