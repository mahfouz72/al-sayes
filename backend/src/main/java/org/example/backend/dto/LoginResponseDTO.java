package org.example.backend.dto;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String token, String role) {
}
