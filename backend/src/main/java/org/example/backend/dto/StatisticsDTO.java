package org.example.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDTO {

    Integer totalUsers;
    Integer totalParkingLots;
    Integer totalRevenue;
    Integer totalViolations;
    Integer monthlyRevenue;

}
