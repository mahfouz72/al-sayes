package org.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotDTO {
    private Long id;
    private int capacity;
    private String location;

    // Longest possible reservation duration
    private double timeLimit;

    // Duration of not showing up, so that the reservation is automatically released
    private double automaticReleaseTime;

    // Penalty of not showing up
    private double notShowingUpPenalty;

    // Penalty for staying parked over reserved time (scale per extra hour)
    private double overTimeScale;
}
