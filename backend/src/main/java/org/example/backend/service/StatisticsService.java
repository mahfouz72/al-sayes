package org.example.backend.service;

import org.example.backend.dao.StatisticsDAO;
import org.example.backend.dto.StatisticsDTO;
import org.example.backend.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    private final StatisticsDAO statisticsDAO;

    public StatisticsService(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    public StatisticsDTO getStatistics() {
        int numberOfUsers = statisticsDAO.countUsers();
        int numberOfParkingLots = statisticsDAO.countParkingLots();
        int totalRevenue = statisticsDAO.getTotalRevenue();
        int totalViolations = statisticsDAO.getTotalViolations();
        int monthlyRevenue = statisticsDAO.getMonthlyRevenue();

        return StatisticsDTO.builder()
                .totalUsers(numberOfUsers)
                .totalParkingLots(numberOfParkingLots)
                .totalRevenue(totalRevenue)
                .totalViolations(totalViolations)
                .monthlyRevenue(monthlyRevenue)
                .build();
    }

    public List<Account> getAllUsers(int page, int size) {
        return statisticsDAO.listAllUsers(page, size);
    }

    public List<Map<String, Object>> getParkingSlotsWithRevenueAndOccupancy(int limit) {
        return statisticsDAO.getParkingSlotsWithRevenueAndOccupancy(limit);
    }

    public List<Map<String, Object>> getTopUsersWithMostReservations(int limit) {
        return statisticsDAO.getTopUsersWithMostReservations(limit);
    }
}
