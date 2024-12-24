package org.example.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingSpotDTO {
    // Spot id inside the Lot
    Long id;
    Long lotId;
    double cost;
    String currentStatus;
    String type;
}
