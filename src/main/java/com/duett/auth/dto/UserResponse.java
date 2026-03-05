package com.duett.auth.dto;

import com.duett.auth.entity.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String cpf,
        Role role
) {}