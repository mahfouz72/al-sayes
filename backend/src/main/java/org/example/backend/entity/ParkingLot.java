package org.example.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ParkingLot {
    private Long id;
    private String name;
    private Long managedBy; // user id

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
