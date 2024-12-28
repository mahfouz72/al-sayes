package org.example.backend.controller;

import net.sf.jasperreports.engine.JRException;
import org.example.backend.dto.StatisticsDTO;
import org.example.backend.dto.UserDetailsDTO;
import org.example.backend.entity.Account;
import org.example.backend.service.ReportServices;
import org.example.backend.service.StatisticsService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;


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

    @PostMapping("/users-report/{report}")
    public ResponseEntity<ByteArrayResource> generateUsersReport(
            @PathVariable String report
    ) {
        try {
            byte[] pdfBytes = reportService.exportReport(report);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + report + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);
        } catch (FileNotFoundException | JRException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

