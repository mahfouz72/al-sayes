package org.example.backend.controller;

import org.example.backend.dao.NotificationDAO;
import org.example.backend.dto.NotificationDTO;
import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.entity.Account;
import org.example.backend.service.AuthenticationService;
import org.example.backend.service.LotManagerService;
import org.example.backend.service.NotificationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.management.Notification;

@RestController
@RequestMapping("/notifications")
@CrossOrigin("http://localhost:3000")
public class NotificationsController {
    private final NotificationService notificationService;
    private final NotificationDAO notificationDAO;
    private final AuthenticationService authenticationService;

    public NotificationsController(NotificationService notificationService, NotificationDAO notificationDAO, AuthenticationService authenticationService) {
        this.notificationService = notificationService;
        this.authenticationService = authenticationService;
        this.notificationDAO = notificationDAO;
    }

    @GetMapping("/")
    public List<NotificationDTO> getNotifications() {
        Optional<Account> account = authenticationService.getCurrentAccount();
        if (account.isPresent()) {
            List<NotificationDTO> notifications = notificationDAO.getNotificationsByAccountId(account.get().getId());
            // delete notifications
            // notificationDAO.deleteNotificationByAccountId(account.get().getId());
            return notifications;
        }
        return List.of();
    }

}
