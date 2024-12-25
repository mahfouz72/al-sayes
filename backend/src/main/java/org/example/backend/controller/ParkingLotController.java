package org.example.backend.controller;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingLotDetails;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.mapper.ParkingLotMapper;
import org.example.backend.service.ParkingLotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lots")
@CrossOrigin(origins = "*")
public class ParkingLotController {
    private ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }
    @GetMapping("/get")
    public ResponseEntity<List<ParkingLotDetails>> listUserManagedLots() {
        Long userId = 1L;
        // TODO: Get Current User ID (Needed ROLES: Manager/Admin)
        // TODO: If admin get all
        List<ParkingLotDetails> lots = this.parkingLotService.findAllParkingLotsByManager(userId);
        if (lots.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lots);
    }
}
