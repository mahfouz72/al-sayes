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
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingSpotService {
    private ParkingSpotDAO parkingSpotDAO;
    private ParkingSpotMapper parkingSpotMapper;

    public void updateParkingSpot(ParkingSpotDTO spotDTO) {
        Pair<Long, Long> pKey = Pair.of(spotDTO.getId(), spotDTO.getLotId());
        parkingSpotDAO.update(pKey,
                parkingSpotMapper.fromDTO(spotDTO));
    }
}
