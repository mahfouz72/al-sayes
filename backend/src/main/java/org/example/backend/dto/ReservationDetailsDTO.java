package org.example.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class is a DTO for the reservation details.
 * It is used to view the reservation details for all
 * users. This will be used in the admin and the manager
 * reservations page.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDetailsDTO {
    private String driverName;
    private String driverEmail;
    private String parkingLot;
    private String spotNumber;
    private String startTime;
    private double duration;
    private String status;
    private double total;
}
