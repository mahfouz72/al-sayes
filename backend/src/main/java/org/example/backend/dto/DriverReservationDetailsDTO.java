package org.example.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class is a DTO for a single driver reservation details.
 * It is used to view the reservation details for a single
 * driver.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverReservationDetailsDTO {
    private String parkingLot;
    private String spotNumber;
    private String startTime;
    private double duration;
    private String status;
    private double total;
}
