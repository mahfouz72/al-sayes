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

    // Run every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void releaseExpiredReservations() {
        // System.out.println("Releasing expired reservations");
        reservationService.releaseExpiredReservations();
    }

    // Run every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void applyPenalties() {
        // System.out.println("Applying penalties");
        reservationService.checkPenalties();
    }
}
