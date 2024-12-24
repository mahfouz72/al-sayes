package org.example.backend.controller;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.service.ParkingLotService;
import org.example.backend.service.ParkingSpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lots/{lot_id}/spots/")
public class ParkingSpotController {
    private ParkingLotService parkingLotService;

    private ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingLotService parkingLotService,
                                 ParkingSpotService parkingSpotService) {
        this.parkingLotService = parkingLotService;
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateSpotStatus(@RequestBody ParkingSpotDTO spotDTO,
                                                          @PathVariable Long lot_id) {
        spotDTO.setLotId(lot_id);
        parkingSpotService.updateParkingSpot(spotDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<ParkingSpotDTO>> listSpotsInParkingLot(@PathVariable Long lot_id) {
        return ResponseEntity.ok(this.parkingLotService.listAllSpotsInLot(lot_id));
    }
}
