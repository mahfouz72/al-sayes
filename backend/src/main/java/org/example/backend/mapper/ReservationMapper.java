package org.example.backend.mapper;

import org.example.backend.dto.ReservationDTO;
import org.example.backend.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public ReservationDTO toDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getDriverId(),
                reservation.getLotId(),
                reservation.getSpotId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getViolationDuration(),
                reservation.getPenalty()                
        );
    }

    public Reservation fromDTO(ReservationDTO dto) {
        return new Reservation(
                dto.getDriverId(),
                dto.getLotId(),
                dto.getSpotId(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getViolationDuration(),
                dto.getPenalty()
        );
    }
}
