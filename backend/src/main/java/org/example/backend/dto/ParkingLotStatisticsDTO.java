package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotStatisticsDTO {
    private Long lotId;
    private Integer totalSpots;
    private Integer occupiedSpots;
    private Integer availableSpots;
    private Integer regularSpots;
    private Integer disabledSpots;
    private Integer evSpots;
    private Double avgPrice;
    private Integer reservations;
    private Double totalRevenue;
    private Integer totalViolations;
    
}
