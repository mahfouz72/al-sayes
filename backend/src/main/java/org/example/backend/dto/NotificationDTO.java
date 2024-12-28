package org.example.backend.dto;

import java.sql.Timestamp;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private Long accountId;
    private Timestamp timestamp;
}
