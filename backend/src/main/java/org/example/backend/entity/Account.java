package org.example.backend.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
}
