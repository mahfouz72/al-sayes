package org.example.backend.service;

import org.example.backend.dao.ReservationDAO;
import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private AuthenticationService authenticationService;
    private ReservationDAO reservationDAO;

    public AdminService(AuthenticationService authenticationService, ReservationDAO reservationDAO) {
        this.authenticationService = authenticationService;
        this.reservationDAO = reservationDAO;
    }

    public List<ReservationDetailsDTO> getReservations() {
        Optional<Account> admin = authenticationService.getCurrentAccount();
        List<ReservationDetailsDTO> adminReservations = null;
        if (admin.isPresent()) {
            adminReservations = reservationDAO.getAllReservations();
        }
        return adminReservations;
    }
}
