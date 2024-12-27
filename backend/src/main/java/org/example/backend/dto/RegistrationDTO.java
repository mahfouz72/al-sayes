package org.example.backend.dto;

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
