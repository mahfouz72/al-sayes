package org.example.backend.service;

import org.example.backend.dao.DriverDAO;
import org.example.backend.dao.ParkingLotDAO;
import org.example.backend.dto.StatisticsDTO;
import org.example.backend.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private final DriverDAO driverDAO;
    private final ParkingLotDAO parkingLotDAO;

    public StatisticsService(DriverDAO driverDAO, ParkingLotDAO parkingLotDAO) {
        this.driverDAO = driverDAO;
        this.parkingLotDAO = parkingLotDAO;
    }

    public StatisticsDTO getStatistics() {
        int numberOfUsers = driverDAO.countUsers();
        int numberOfParkingLots = parkingLotDAO.countParkingLots();

        return StatisticsDTO.builder()
                .totalUsers(numberOfUsers)
                .totalParkingLots(numberOfParkingLots)
                .build();
    }

    public List<Account> getAllUsers(int page, int size) {
        return driverDAO.listAllUsers(page, size);
    }
}
