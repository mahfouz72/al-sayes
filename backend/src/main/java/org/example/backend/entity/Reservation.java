package org.example.backend.entity;

import java.sql.Timestamp;

import org.example.backend.enums.ReservationStatus;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
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
