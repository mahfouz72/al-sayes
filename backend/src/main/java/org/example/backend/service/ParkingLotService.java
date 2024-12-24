package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dao.ParkingLotDAO;
import org.example.backend.dao.ParkingSpotDAO;
import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.entity.ParkingLot;
import org.example.backend.entity.ParkingSpot;
import org.example.backend.mapper.ParkingLotMapper;
import org.example.backend.mapper.ParkingSpotMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingLotService {
    private ParkingLotDAO parkingLotDAO;
    private ParkingSpotDAO parkingSpotDAO;
    private ParkingLotMapper parkingLotMapper;

    private ParkingSpotMapper parkingSpotMapper;

    public Optional<ParkingLotDTO> findParkingLotById(Long id) {
        Optional<ParkingLot> parkingLot = parkingLotDAO.getByPK(id);
        Optional<ParkingLotDTO> parkingLotDTO = Optional.empty();
        if (parkingLot.isPresent()) {
            parkingLotDTO = Optional.of(parkingLotMapper.toDTO(parkingLot.get()));
        }
        return parkingLotDTO;
    }

    public List<ParkingSpotDTO> listAllSpotsInLot(long lot_id) {
        List<ParkingSpot> requestedSpots = parkingSpotDAO.listAllSpotsFilterByLotId(lot_id);
        List<ParkingSpotDTO> requestedSpotsDTO = new ArrayList<>();
        for (ParkingSpot spot : requestedSpots) {
            requestedSpotsDTO.add(parkingSpotMapper.toDTO(spot));
        }
        return requestedSpotsDTO;
    }
}
