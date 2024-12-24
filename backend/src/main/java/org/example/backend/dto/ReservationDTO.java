package org.example.backend.dto;
import java.sql.Timestamp;
import java.sql.Time;

import org.example.backend.enums.ReservationStatus;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long driverId;
    private Long lotId;
    private Long spotId;
    private Timestamp startTime;
    private Timestamp endTime;
    private double price;
    private ReservationStatus status;
    private double violationDuration;
    private double penalty;
}
