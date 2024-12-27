package org.example.backend.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotCard {
    private Long id;
    private String name;
    private String location;
    private double latitude;
    private double longitude;
    private double averagePrice;
    private int totalSpots;
    private int availableSpots;
    private int regularSpots;
    private int disabledSpots;
    private int evSpots;
    private double timeLimit;
}
