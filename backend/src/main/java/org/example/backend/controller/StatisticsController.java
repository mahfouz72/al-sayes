package org.example.backend.controller;

import org.example.backend.dto.StatisticsDTO;
import org.example.backend.entity.Account;
import org.example.backend.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/totals")
    public ResponseEntity<StatisticsDTO> getStatistics() {
        StatisticsDTO statistics = statisticsService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Account> users = statisticsService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/top-parking-lots")
    public ResponseEntity<?> getParkingSlotsWithRevenueAndOccupancy(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<?> parkingSlots = statisticsService.getParkingSlotsWithRevenueAndOccupancy(limit);
        return ResponseEntity.ok(parkingSlots);
    }

    @PostMapping("/top-users")
    public ResponseEntity<?> getTopUsersWithMostReservations(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<?> topUsers = statisticsService.getTopUsersWithMostReservations(limit);
        return ResponseEntity.ok(topUsers);
    }
}

