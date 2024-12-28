package org.example.backend.dto;

import org.example.backend.enums.PaymentMethod;

import lombok.Builder;
import org.example.backend.enums.PaymentMethod;

@Builder
public record RegistrationDTO(
        String username,
        String email,
        String password,
        String role,
        String licensePlate,
        PaymentMethod paymentMethod) {
}
