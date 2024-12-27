package org.example.backend.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetails {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String licensePlate;
    private String paymentMethod;
}
