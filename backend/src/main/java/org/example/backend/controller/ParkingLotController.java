package org.example.backend.controller;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.mapper.ParkingLotMapper;
import org.example.backend.service.ParkingLotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lots/")
public class ParkingLotController {
    private ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @GetMapping("/{lot_id}")
    public ResponseEntity<ParkingLotDTO> viewDetailsOfParkingLot(@PathVariable Long lot_id) {
        Optional<ParkingLotDTO> requestedLot = this.parkingLotService.findParkingLotById(lot_id);
        return requestedLot.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
