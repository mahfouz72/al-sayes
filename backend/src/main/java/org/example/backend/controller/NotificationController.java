package org.example.backend.controller;

import org.example.backend.entity.Notification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/sendNotifications")
    public void sendNotification(@RequestBody Notification notification) {
        simpMessagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
