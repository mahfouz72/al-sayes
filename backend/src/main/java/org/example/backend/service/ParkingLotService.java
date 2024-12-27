package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dao.ParkingLotDAO;
import org.example.backend.dao.ParkingSpotDAO;
import org.example.backend.dao.ReservationDAO;
import org.example.backend.dto.ParkingLotCard;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingLotService {
    private ParkingLotDAO parkingLotDAO;
    private ParkingSpotDAO parkingSpotDAO;
    private ReservationDAO  reservationDAO;
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

    public List<ParkingSpotDTO> listAllSpotsInLotAvailable(long lot_id, Timestamp start, Timestamp end) {
        List<ParkingSpot> spots = parkingSpotDAO.listAllSpotsFilterByLotId(lot_id);
        List<ParkingSpot> availableSpots = new ArrayList<>();
        for (ParkingSpot spot : spots) {
            if (reservationDAO.isSpotAvailable(lot_id, spot.getId(), start, end)) {
                availableSpots.add(spot);
            }
        }
        List<ParkingSpotDTO> availableSpotsDTO = new ArrayList<>();
        for (ParkingSpot spot : availableSpots) {
            ParkingSpotDTO spotDTO = parkingSpotMapper.toDTO(spot);
            spotDTO.setCurrentStatus("AVAILABLE");
            availableSpotsDTO.add(spotDTO);
        }
        return availableSpotsDTO;
    }

    public List<ParkingLotDetails> findAllParkingLotsByManager(long managerId) {
        return parkingLotDAO.getLotsDetailsByManagerId(managerId);
    }

    public Long createParkingLot(ParkingLotDTO parkingLotDTO, Long id) {
        Long lotId = parkingLotDAO.insertAndReturnKey(parkingLotMapper.fromDTO(parkingLotDTO, id));
        if (lotId == null) {
            return -1L;
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
        return lotId;
    }

    public List<ParkingLotCard> findAllParkingLotsCards() {
        return parkingLotDAO.getLotsCards();
    }

    public boolean deleteParkingLot(Long lotId) {
        Optional<ParkingLot> lot = parkingLotDAO.getByPK(lotId);
        if (lot.isEmpty()) {
            return false;
        }
        parkingLotDAO.delete(lotId);
        return true;
    }
}
