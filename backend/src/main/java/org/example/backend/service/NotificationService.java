package org.example.backend.service;

import lombok.Setter;
import org.example.backend.controller.NotificationController;
import org.example.backend.dao.AccountDAO;
import org.example.backend.dao.ParkingLotDAO;
import org.example.backend.dao.ReservationDAO;
import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.entity.Account;
import org.example.backend.entity.Notification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Setter
    private String currentUserName;
    private final NotificationController notificationController;
    private final AccountDAO accountDAO;
    private ReservationDAO reservationDAO;
    private ParkingLotDAO parkingLotDAO;

    public NotificationService(NotificationController notificationController, AccountDAO accountDAO,
                               ReservationDAO reservationDAO, ParkingLotDAO parkingLotDAO) {
        this.notificationController = notificationController;
        this.accountDAO = accountDAO;
        this.reservationDAO = reservationDAO;
        this.parkingLotDAO = parkingLotDAO;
    }

    @Scheduled(fixedRate = 900000)
    public void sendNotification() {
        Optional<Account> account = accountDAO.getByUsername(currentUserName);
        if (account.isPresent()) {
            long accountId = account.get().getId();
            String role = accountDAO.getRoleByUsername(currentUserName);
            if (role.equals("ROLE_DRIVER")) {
                notifyNearEndTime(accountId);
            } else if (role.equals("ROLE_MANAGER")) {
                // notify with faulty spot
            }
        }
    }

    private void notifyNearEndTime(long accountId) {
        List<ReservationDetailsDTO> reservations = reservationDAO.getReservationsNearEndTime(accountId);
        for (ReservationDetailsDTO reservation : reservations) {
            String message = String.format("Your reservation at %s (%s) is about to end.",
                    reservation.getParkingLot(),
                    reservation.getSpotNumber());
            Notification notification = new Notification(message);
            notificationController.sendNotification(notification);
        }
    }

    private void notifyNearStartTime(long accountId) {
        List<ReservationDetailsDTO> reservations = reservationDAO.getReservationsNearStartTime(accountId);
        for (ReservationDetailsDTO reservation : reservations) {
            String message = String.format("Your reservation at %s (%s) is about to start.",
                    reservation.getParkingLot(),
                    reservation.getSpotNumber());
            Notification notification = new Notification(message);
            notificationController.sendNotification(notification);
        }
    }

    public void notifyNotShowingUpPenalty(Long lotId, Long spotId, double penalty) {
        String parkingLotName = parkingLotDAO.getParkingLotNameById(lotId);

        String message = String.format("You have been charged a penalty of (%s $) for not showing up at %s (%s).",
                penalty,
                parkingLotName,
                "S" + spotId);
        Notification notification = new Notification(message);
        notificationController.sendNotification(notification);
    }
}
