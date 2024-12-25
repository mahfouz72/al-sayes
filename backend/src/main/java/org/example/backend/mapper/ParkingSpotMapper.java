package org.example.backend.mapper;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.entity.ParkingSpot;
import org.springframework.stereotype.Component;

@Component
public class ParkingSpotMapper {
    public ParkingSpotDTO toDTO(ParkingSpot parkingSpot) {
        return ParkingSpotDTO.builder()
                .id(parkingSpot.getId())
                .lotId(parkingSpot.getLotId())
                .currentStatus(parkingSpot.getCurrentStatus())
                .cost(parkingSpot.getCost())
                .type(parkingSpot.getType())
                .build();
    }

    public ParkingSpot fromDTO(ParkingSpotDTO dto) {
        return new ParkingSpot(
                dto.getId(),
                dto.getLotId(),
                dto.getCost(),
                dto.getCurrentStatus(),
                dto.getType()
        );
    }
}
