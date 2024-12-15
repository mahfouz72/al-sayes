package org.example.backend.dto;

import lombok.Builder;

@Builder
public record DriverRegistrationDTO(
        String username,
        String email,
        String password,
        String licensePlate) {
}
