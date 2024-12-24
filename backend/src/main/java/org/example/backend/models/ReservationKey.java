package org.example.backend.models;

import java.sql.Timestamp;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
public class ReservationKey {
    private Long driverId;
    private Long lotId;
    private Long spotId;
    private Timestamp startTime;  
    
}
