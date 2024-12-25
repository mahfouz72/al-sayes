package org.example.backend.controller;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Optional;

import org.example.backend.dao.AccountDAO;
import org.example.backend.dto.ReservationDTO;
import org.example.backend.entity.Account;
import org.example.backend.enums.ReservationStatus;
import org.example.backend.service.ReservationService;
import org.example.backend.service.AuthenticationService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final AuthenticationService authenticationService;
    private final AccountDAO accountDAO;

    public ReservationController(ReservationService reservationService, AuthenticationService authenticationService, AccountDAO accountDAO) {
        this.reservationService = reservationService;
        this.authenticationService = authenticationService;
        this.accountDAO = accountDAO;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createReservation(@RequestBody ReservationDTO reservationDTO) {
        String username = authenticationService.getCurrentUsername();
        Optional<Account> account = accountDAO.getByUsername(username);
        if (account.isPresent()) {
            reservationDTO.setDriverId(account.get().getId());
            // reservationDTO.setStatus(ReservationStatus.PENDING);
            // reservationDTO.setViolationDuration(0);
            // reservationDTO.setPenalty(0);
            if (reservationService.reserveSpot(reservationDTO)) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
    
}
