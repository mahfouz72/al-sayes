package org.example.backend.service;

import lombok.Setter;
import org.example.backend.controller.NotificationController;
import org.example.backend.dao.AccountDAO;
import org.example.backend.dao.NotificationDAO;
import org.example.backend.dao.ParkingLotDAO;
import org.example.backend.dao.ReservationDAO;
import org.example.backend.dto.NotificationDTO;
import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.entity.Account;
import org.example.backend.entity.Notification;
import org.springframework.data.relational.core.sql.Not;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    private final NotificationDAO notificationDAO;

    public NotificationService(NotificationController notificationController, AccountDAO accountDAO,
                               ReservationDAO reservationDAO, ParkingLotDAO parkingLotDAO, NotificationDAO notificationDAO) {
        this.notificationController = notificationController;
        this.accountDAO = accountDAO;
        this.reservationDAO = reservationDAO;
        this.parkingLotDAO = parkingLotDAO;
        this.notificationDAO = notificationDAO;
    }
    // every 30 seconds
    @Scheduled(fixedRate = 300000)
    public void sendNotification() {
        System.out.println("Sending notifications...");
        Optional<Account> account = accountDAO.getByUsername(currentUserName);
        if (account.isPresent()) {
            long accountId = account.get().getId();
            String role = accountDAO.getRoleByUsername(currentUserName);
            if (role.equals("ROLE_DRIVER")) {
                notifyNearStartTime(accountId);
                notifyNearEndTime(accountId);
            } else if (role.equals("ROLE_MANAGER")) {
                // notify with faulty spot
            }
        }
    }

    private void notifyNearEndTime(long accountId) {
        
        // NotificationDTO notificationDTO = new NotificationDTO();
        // notificationDTO.setMessage("Your reservation is about to end.");
        // notificationDTO.setAccountId(accountId);
        // notificationDTO.setTimestamp(new Timestamp(System.currentTimeMillis()));
        // notificationDAO.insert(notificationDTO);
        // Notification notification = new Notification("Your reservation is about to end.");
        //     notificationController.sendNotification(notification);
        // System.out.println("Notification sent: Your reservation is about to end.");

        List<ReservationDetailsDTO> reservations = reservationDAO.getReservationsNearEndTime(accountId);
        for (ReservationDetailsDTO reservation : reservations) {
            String message = String.format("Your reservation at %s (%s) is about to end.",
                    reservation.getParkingLot(),
                    reservation.getSpotNumber());
            Notification notification = new Notification(message);
            notificationController.sendNotification(notification);
            notificationController.sendNotification(notification);
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage(message);
            notificationDTO.setAccountId(accountId);
            notificationDTO.setTimestamp(new Timestamp(System.currentTimeMillis()));
            notificationDAO.insert(notificationDTO);
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
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage(message);
            notificationDTO.setAccountId(accountId);
            notificationDTO.setTimestamp(new Timestamp(System.currentTimeMillis()));
            notificationDAO.insert(notificationDTO);
            System.out.println("Notification sent: " + message);
        }
    }

    public void notifyNotShowingUpPenalty(Long driverId, Long lotId, Long spotId, double penalty, Long managerId) {
        String parkingLotName = parkingLotDAO.getParkingLotNameById(lotId);

        String message = String.format("You have been charged a penalty of (%s $) for not showing up at %s (%s).",
                penalty,
                parkingLotName,
                "S" + spotId);
        Notification notification = new Notification(message);
        notificationController.sendNotification(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(message);
        notificationDTO.setAccountId(driverId);
        notificationDTO.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationDAO.insert(notificationDTO);
        message = String.format("Driver %s has been charged a penalty of (%s $) for not showing up at %s (%s).",
                driverId,
                penalty,
                parkingLotName,
                "S" + spotId);
        Notification notification2 = new Notification(message);
        notificationController.sendNotification(notification2);
        notificationDTO.setMessage(message);
        notificationDTO.setAccountId(managerId);
        notificationDTO.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationDAO.insert(notificationDTO);
        notificationController.sendNotification(notification2);
    }
}
