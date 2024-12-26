package org.example.backend.service;

import org.example.backend.dao.StatisticsDAO;
import org.example.backend.dto.StatisticsDTO;
import org.example.backend.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private final StatisticsDAO statisticsDAO;

    public StatisticsService(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    public StatisticsDTO getStatistics() {
        int numberOfUsers = statisticsDAO.countUsers();
        int numberOfParkingLots = statisticsDAO.countParkingLots();

        return StatisticsDTO.builder()
                .totalUsers(numberOfUsers)
                .totalParkingLots(numberOfParkingLots)
                .build();
    }

    public List<Account> getAllUsers(int page, int size) {
        return statisticsDAO.listAllUsers(page, size);
    }
}
