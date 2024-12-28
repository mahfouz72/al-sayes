package org.example.backend.controller;

import org.example.backend.dto.StatisticsDTO;
import org.example.backend.dto.UserDetailsDTO;
import org.example.backend.entity.Account;
import org.example.backend.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
        List<UserDetailsDTO> users = statisticsService.getAllUsers(page, size);
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

    @PostMapping("/daily-revenue")
    public ResponseEntity<?> getDailyRevenue(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<?> dailyRevenue = statisticsService.getDailyRevenue(limit);
        return ResponseEntity.ok(dailyRevenue);
    }

    @PostMapping("/daily-reserved-spots")
    public ResponseEntity<?> getDailyReservedSpots(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<?> dailyReservedSpots = statisticsService.getDailyReservedSpots(limit);
        return ResponseEntity.ok(dailyReservedSpots);
    }

    @PostMapping("/block-user")
    public ResponseEntity<?> blockUser(@RequestParam String username) {
        System.out.println(username);
        statisticsService.blockUser(username);
        return ResponseEntity.ok("User blocked successfully");
    }

    @PostMapping("/unblock-user")
    public ResponseEntity<?> unblockUser(@RequestParam String username) {
        statisticsService.unblockUser(username);
        return ResponseEntity.ok("User unblocked successfully");
    }

    @PostMapping("/change-role")
    public ResponseEntity<?> changeRole(@RequestParam String username, @RequestParam String role) {
        statisticsService.changeUserRole(username, role);
        return ResponseEntity.ok("Role changed successfully");
    }
}

