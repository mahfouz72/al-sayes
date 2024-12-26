package org.example.backend.controller;

import org.example.backend.dto.ParkingLotCard;
import org.example.backend.dto.ParkingLotDTO;
import org.example.backend.dto.ParkingLotDetails;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.entity.Account;
import org.example.backend.mapper.ParkingLotMapper;
import org.example.backend.service.AuthenticationService;
import org.example.backend.service.ParkingLotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
        Optional<Account> currentUserOptional = authenticationService.getCurrentAccount();
        if (currentUserOptional.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        Account currentUser = currentUserOptional.get();
        if (!"ROLE_MANAGER".equals(currentUser.getRole())
            && !"ROLE_ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(401).build();
        }
        List<ParkingLotDetails> lots = this.parkingLotService.findAllParkingLotsByManager(currentUser.getId());
        if (lots.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(lots);
    }
    @GetMapping("/get/cards")
    public ResponseEntity<List<ParkingLotCard>> listParkingLotsCards() {
        List<ParkingLotCard> lots = this.parkingLotService.findAllParkingLotsCards();
        if (lots.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lots);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createParkingLot(@RequestBody ParkingLotDTO parkingLotDTO) {
        Optional<Account> currentUserOptional = authenticationService.getCurrentAccount();
        if (currentUserOptional.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        Account currentUser = currentUserOptional.get();
        if (!"ROLE_MANAGER".equals(currentUser.getRole())
        && !"ROLE_ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(401).build();
        }
        Long id = this.parkingLotService.createParkingLot(parkingLotDTO, currentUser.getId());
        if (id != -1) {
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.status(400).build();
    }
}
