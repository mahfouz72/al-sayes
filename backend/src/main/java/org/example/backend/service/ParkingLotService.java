package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dao.ParkingLotDAO;
import org.example.backend.dao.ParkingSpotDAO;
import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingLotDetails;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.dto.ParkingTypeDetails;
import org.example.backend.entity.ParkingLot;
import org.example.backend.entity.ParkingSpot;
import org.example.backend.enums.ParkingType;
import org.example.backend.mapper.ParkingLotMapper;
import org.example.backend.mapper.ParkingSpotMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingLotService {
    private ParkingLotDAO parkingLotDAO;
    private ParkingSpotDAO parkingSpotDAO;
    private ParkingLotMapper parkingLotMapper;

    private ParkingSpotMapper parkingSpotMapper;

    public List<ParkingSpotDTO> listAllSpotsInLot(long lot_id) {
        List<ParkingSpot> requestedSpots = parkingSpotDAO.listAllSpotsFilterByLotId(lot_id);
        List<ParkingSpotDTO> requestedSpotsDTO = new ArrayList<>();
        for (ParkingSpot spot : requestedSpots) {
            requestedSpotsDTO.add(parkingSpotMapper.toDTO(spot));
        }
        return requestedSpotsDTO;
    }

    public List<ParkingLotDetails> findAllParkingLotsByManager(long managerId) {
        return parkingLotDAO.getLotsDetailsByManagerId(managerId);
    }

    public boolean createParkingLot(ParkingLotDTO parkingLotDTO, Long id) {
        Long lotId = parkingLotDAO.insertAndReturnKey(parkingLotMapper.fromDTO(parkingLotDTO, id));
        if (lotId == null) {
            return false;
        }
        Long index = 1L;
        for (Map.Entry<ParkingType, ParkingTypeDetails> entry : parkingLotDTO.getParkingTypes().entrySet()) {
            int typeCapacity = entry.getValue().getCapacity();
            double typePrice = entry.getValue().getBasePricePerHour();
            for (int i = 0; i < typeCapacity; i++) {
                parkingSpotDAO.insert(ParkingSpot.builder()
                        .lotId(lotId)
                        .id(index)
                        .type(entry.getKey().name())
                        .cost(typePrice)
                        .currentStatus("AVAILABLE")
                        .build());
                index++;
            }
        }
        return true;
    }
}
