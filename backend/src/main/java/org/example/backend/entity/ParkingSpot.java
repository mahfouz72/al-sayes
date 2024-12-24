package org.example.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParkingSpot {
    Long id;
    Long lotId;
    double cost;
    String currentStatus;
    String type;
}
