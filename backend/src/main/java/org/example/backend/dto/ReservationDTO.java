package org.example.backend.dto;
import java.sql.Timestamp;
import java.time.Instant;
import java.sql.Time;

import org.example.backend.enums.ReservationStatus;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    public ReservationDTO(Long driverId, Long lotId, Long spotId, String startTime, String endTime, double price,
            ReservationStatus status, double violationDuration, double penalty) {
        this.driverId = driverId;
        this.lotId = lotId;
        this.spotId = spotId;
        this.startTime = Timestamp.from(Instant.parse(startTime));
        this.endTime = Timestamp.from(Instant.parse(endTime));
        this.price = price;
        this.status = status;
        this.violationDuration = violationDuration;
        this.penalty = penalty;
    }
}
