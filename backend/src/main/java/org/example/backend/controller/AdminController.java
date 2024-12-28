package org.example.backend.controller;


import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/reservations")
    public List<ReservationDetailsDTO> getReservations() {
        return adminService.getReservations();
    }
}
