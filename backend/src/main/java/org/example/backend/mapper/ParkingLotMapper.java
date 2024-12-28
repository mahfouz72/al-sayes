package org.example.backend.mapper;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.entity.ParkingLot;
import org.springframework.stereotype.Component;

@Component
public class ParkingLotMapper {
    // Converts a ParkingLotDTO to a ParkingLot entity
    public ParkingLot fromDTO(ParkingLotDTO parkingLotDTO, Long managerId) {
        return ParkingLot.builder()
                .id(parkingLotDTO.getId())
                .name(parkingLotDTO.getName())
                .managedBy(managerId)
                .location(parkingLotDTO.getLocation())
                .latitude(parkingLotDTO.getLatitude())
                .longitude(parkingLotDTO.getLongitude())
                .timeLimit(parkingLotDTO.getTimeLimit())
                .notShowingUpPenalty(parkingLotDTO.getNotShowingUpPenalty())
                .automaticReleaseTime(parkingLotDTO.getAutomaticReleaseTime())
                .overTimeScale(parkingLotDTO.getOverTimeScale())
                .build();
    }

}
