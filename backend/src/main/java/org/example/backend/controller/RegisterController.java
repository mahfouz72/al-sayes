package org.example.backend.controller;


import org.example.backend.dto.DriverRegistrationDTO;
import org.example.backend.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    private final AuthenticationService authenticationService;

    public RegisterController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public void register(@RequestBody DriverRegistrationDTO driverRegistrationDTO) {
        authenticationService.register(driverRegistrationDTO);
    }
}
