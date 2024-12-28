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
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
    private double calculateDynamicPricing(double baseCost, Timestamp start, Timestamp end,
                                           int totalSpots, int availableSpots) {
        double dynamicPrice = baseCost;

        // 1. Time-based Pricing (increase price during peak hours)
        dynamicPrice = applyTimeBasedPricing(dynamicPrice, start, end);

        // 2. Demand-based Pricing (higher prices when fewer spots are available)
        dynamicPrice = applyDemandBasedPricing(dynamicPrice, totalSpots, availableSpots);
        return dynamicPrice;
    }

    private double applyDemandBasedPricing(double currentPrice, int totalSpots, int availableSpots) {
        // Price increases when fewer spots are available
        double occupancyRate = 1.0 - (double) availableSpots / totalSpots;

        if (occupancyRate > 0.8) { // If 80% or more of the spots are occupied
            currentPrice *= 1.3; // Increase by 30%
        }
        return currentPrice;
    }

    private double applyTimeBasedPricing(double currentPrice, Timestamp start, Timestamp end) {
        // Convert Timestamp to Java Calendar/LocalDateTime to easily extract hour
        LocalDateTime startTime = start.toLocalDateTime();
        LocalDateTime endTime = end.toLocalDateTime();
        final double WEEKENDS_SCALE = 1.1;
        final double PEAK_HOURS_SCALE = 1.2;
        // Increase prices during peak hours (12 PM to 6 PM)
        int startHour = startTime.getHour();
        int endHour = endTime.getHour();

        if ((startHour >= 12 && startHour <= 18) || (endHour >= 12 && endHour <= 18)) {
            currentPrice *= PEAK_HOURS_SCALE; // Increase by 20% during peak hours
        }

        // Weekend pricing (Friday/Saturday)
        DayOfWeek startDay = startTime.getDayOfWeek();
        if (startDay == DayOfWeek.FRIDAY || startDay == DayOfWeek.SATURDAY) {
            currentPrice *= WEEKENDS_SCALE; // Increase by 10% on weekends
        }
        return currentPrice;
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
            // Dynamic Pricing:
            spotDTO.setCost(calculateDynamicPricing(spot.getCost(), start,end,
                    spots.size(), availableSpots.size()));
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

    public boolean updateParkingLot(ParkingLotDTO parkingLotDTO, Long id) {
        Optional<ParkingLot> existingLot = parkingLotDAO.getByPK(id);
        // Check if the parking lot exists
        if (existingLot.isEmpty()) {
            System.out.println("ID: " + id + " not found");
            return false;
        }

        ParkingLot lotToUpdate = parkingLotMapper.fromDTO(parkingLotDTO, id);
        lotToUpdate.setName(parkingLotDTO.getName());
        lotToUpdate.setLocation(parkingLotDTO.getLocation());
        lotToUpdate.setLatitude(parkingLotDTO.getLatitude());
        lotToUpdate.setLongitude(parkingLotDTO.getLongitude());
        parkingLotDAO.update(lotToUpdate.getId(), lotToUpdate);
        return true;
    }

    public List<ParkingLotCard> findAllParkingLotsCardsOfManager(Long managerId) {
         return parkingLotDAO.getLotsCardsOfManager(managerId);
    }
}
