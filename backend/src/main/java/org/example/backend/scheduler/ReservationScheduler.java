package org.example.backend.scheduler;

import org.example.backend.service.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    private final ReservationService reservationService;

    public ReservationScheduler(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Run every 15 minutes
    @Scheduled(fixedRate = 900000)
    public void releaseExpiredReservations() {
        reservationService.releaseExpiredReservations();
    }

    // Run every 15 minutes
    @Scheduled(fixedRate = 900000)
    public void applyPenalties() {
        reservationService.checkPenalties();
    }
}
