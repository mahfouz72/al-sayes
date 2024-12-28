package org.example.backend.service;

import org.example.backend.dao.ReservationDAO;
import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LotManagerService {

    private final AuthenticationService authenticationService;
    private ReservationDAO reservationDAO;

    public LotManagerService(AuthenticationService authenticationService, ReservationDAO reservationDAO) {
        this.authenticationService = authenticationService;
        this.reservationDAO = reservationDAO;
    }

    public List<ReservationDetailsDTO> getReservations() {
        Optional<Account> lotManager = authenticationService.getCurrentAccount();
        List<ReservationDetailsDTO> managerReservations = null;
        if (lotManager.isPresent()) {
            long lotManagerId = lotManager.get().getId();
            managerReservations = reservationDAO.getReservationsByLotManagerID(lotManagerId);
        }
        return managerReservations;
    }
}
