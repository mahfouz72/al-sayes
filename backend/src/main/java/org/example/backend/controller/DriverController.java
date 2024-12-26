package org.example.backend.controller;

import org.example.backend.dto.DriverReservationDetailsDTO;
import org.example.backend.service.DriverService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@CrossOrigin(origins = "http://localhost:3000")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/reservations")
    public List<DriverReservationDetailsDTO> getReservations() {
        return driverService.getReservations();
    }
}
