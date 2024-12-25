package org.example.backend.mapper;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.entity.ParkingLot;
import org.springframework.stereotype.Component;

@Component
public class ParkingLotMapper {

    // Converts a ParkingLot entity to a ParkingLotDTO
    public ParkingLotDTO toDTO(ParkingLot parkingLot) {
        return  ParkingLotDTO.builder()
                .id(parkingLot.getId())
                .name(parkingLot.getName())
                .managedBy(parkingLot.getManagedBy())
                .capacity(parkingLot.getCapacity())
                .location(parkingLot.getLocation())
                .timeLimit(parkingLot.getTimeLimit())
                .notShowingUpPenalty(parkingLot.getNotShowingUpPenalty())
                .automaticReleaseTime(parkingLot.getAutomaticReleaseTime())
                .overTimeScale(parkingLot.getOverTimeScale())
                .build();
    }

    // Converts a ParkingLotDTO to a ParkingLot entity
    public ParkingLot fromDTO(ParkingLotDTO parkingLotDTO) {
        return new ParkingLot(parkingLotDTO.getId(),
                parkingLotDTO.getName(),
                parkingLotDTO.getManagedBy(),
                parkingLotDTO.getCapacity(),
                parkingLotDTO.getLocation(),
                parkingLotDTO.getTimeLimit(),
                parkingLotDTO.getAutomaticReleaseTime(),
                parkingLotDTO.getNotShowingUpPenalty(),
                parkingLotDTO.getOverTimeScale());
    }

}
