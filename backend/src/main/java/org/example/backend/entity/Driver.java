package org.example.backend.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    private Long id;
    private String licensePlate;
    private Account account;
}
