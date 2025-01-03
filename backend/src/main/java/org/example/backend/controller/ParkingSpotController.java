package org.example.backend.controller;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.service.ParkingLotService;
import org.example.backend.service.ParkingSpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots")
@CrossOrigin(origins = "http://localhost:5173")
public class ParkingSpotController {
    private final ParkingLotService parkingLotService;

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingLotService parkingLotService,
                                 ParkingSpotService parkingSpotService) {
        this.parkingLotService = parkingLotService;
        this.parkingSpotService = parkingSpotService;
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateSpotStatus(@RequestBody ParkingSpotDTO spotDTO) {
        parkingSpotService.updateParkingSpot(spotDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lot_id}/get")
    public ResponseEntity<List<ParkingSpotDTO>> listSpotsInParkingLot(@PathVariable Long lot_id) {
        return ResponseEntity.ok(this.parkingLotService.listAllSpotsInLot(lot_id));
    }

    @GetMapping("/{lot_id}/get/available")
    public ResponseEntity<List<ParkingSpotDTO>> listAvailableSpotsInParkingLot(@PathVariable Long lot_id, @RequestParam String start, @RequestParam String end) {
        Timestamp startTime = Timestamp.from(Instant.parse(start));
        Timestamp endTime = Timestamp.from(Instant.parse(end));
        return ResponseEntity.ok(this.parkingLotService.listAllSpotsInLotAvailable(lot_id, startTime, endTime));
    }
}
