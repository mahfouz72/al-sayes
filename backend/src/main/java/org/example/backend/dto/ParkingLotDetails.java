package org.example.backend.dto;

import java.util.List;

import org.example.backend.entity.ParkingSpot;

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
public class ParkingLotDetails {
    private Long id;
    private String name;
    private int capacity;
    private String location;
    private double occupancyRate;
    private double revenue;
    private double violations;
}
