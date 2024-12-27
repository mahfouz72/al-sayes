package org.example.backend.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.example.backend.dto.ParkingSpotDTO;
import org.example.backend.service.ParkingLotService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ParkingSpotWSController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ParkingLotService parkingLotService;

    public ParkingSpotWSController(SimpMessagingTemplate messagingTemplate, ParkingLotService parkingLotService) {
        this.messagingTemplate = messagingTemplate;
        this.parkingLotService = parkingLotService;
    }

    @MessageMapping("/spots/{lotId}")
    public void sendParkingSpots(@DestinationVariable Long lotId) {
        List<ParkingSpotDTO> spots = parkingLotService.listAllSpotsInLot(lotId);
        messagingTemplate.convertAndSend("/topic/spots/" + lotId, spots);
    }
}
