package org.example.backend.controller;

import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.service.LotManagerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lot-manager")
@CrossOrigin("http://localhost:3000")
public class LotManagerController {
    private final LotManagerService lotManagerService;

    public LotManagerController(LotManagerService lotManagerService) {
        this.lotManagerService = lotManagerService;
    }

    @GetMapping("/reservations")
    public List<ReservationDetailsDTO> getReservations() {
        return lotManagerService.getReservations();
    }
}
