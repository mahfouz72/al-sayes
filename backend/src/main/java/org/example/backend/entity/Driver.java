package org.example.backend.entity;

import org.example.backend.enums.PaymentMethod;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    private Long id;
    private String licensePlate;
    private Account account;
    private PaymentMethod paymentMethod;
}
