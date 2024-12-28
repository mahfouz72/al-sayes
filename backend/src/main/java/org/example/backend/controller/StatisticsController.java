package org.example.backend.controller;

import net.sf.jasperreports.engine.JRException;
import org.example.backend.dto.StatisticsDTO;
import org.example.backend.dto.UserDetailsDTO;
import org.example.backend.entity.Account;
import org.example.backend.service.ReportServices;
import org.example.backend.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ReportServices reportService;

    public StatisticsController(StatisticsService statisticsService, ReportServices reportService) {
        this.statisticsService = statisticsService;
        this.reportService = reportService;
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
        statisticsService.blockUser(username);
        return ResponseEntity.ok("User blocked successfully");
    }

    @PostMapping("/unblock-user")
    public ResponseEntity<?> unblockUser(@RequestParam String username) {
        statisticsService.unblockUser(username);
        return ResponseEntity.ok("User unblocked successfully");
    }

    @PostMapping("/users-report")
    public ResponseEntity<?> generateUsersReport(
            @RequestParam String report
    ) throws JRException, FileNotFoundException {
        reportService.exportReport(report);
        return ResponseEntity.ok("Report generated successfully");
    }


}

