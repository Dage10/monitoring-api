package com.david.monitoring.users.dto;

public record CreateUserRequest(
        String username,
        String email,
        String password
) {}
