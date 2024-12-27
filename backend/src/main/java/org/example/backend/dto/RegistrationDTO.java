package org.example.backend.dto;

import org.example.backend.enums.PaymentMethod;

import lombok.Builder;

@Builder
public record RegistrationDTO(
        String username,
        String email,
        String password,
        String role,
        String licensePlate,
        PaymentMethod paymentMethod) {
}
