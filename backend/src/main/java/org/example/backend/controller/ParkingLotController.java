package org.example.backend.controller;

import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingLotDetails;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.entity.Account;
import org.example.backend.mapper.ParkingLotMapper;
import org.example.backend.service.AuthenticationService;
import org.example.backend.service.ParkingLotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lots")
@CrossOrigin(origins = "http://localhost:5173")
public class ParkingLotController {
    private final ParkingLotService parkingLotService;
    private final AuthenticationService authenticationService;

    public ParkingLotController(ParkingLotService parkingLotService, AuthenticationService authenticationService) {
        this.parkingLotService = parkingLotService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<ParkingLotDetails>> listUserManagedLots() {
        Account currentUser = authenticationService.getCurrentAccount();
        if ("ROLE_MANAGER".equals(currentUser.getRole())) {
            return ResponseEntity.status(401).build();
        }
        List<ParkingLotDetails> lots = this.parkingLotService.findAllParkingLotsByManager(currentUser.getId());
        if (lots.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lots);
    }
}
