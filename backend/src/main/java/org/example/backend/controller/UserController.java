package org.example.backend.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.example.backend.dao.AccountDAO;
import org.example.backend.dto.UserDetails;
import org.example.backend.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {
    private final AuthenticationService authenticationService;
    private final AccountDAO accountDAO;

    public UserController(AuthenticationService authenticationService, AccountDAO accountDAO) {
        this.authenticationService = authenticationService;
        this.accountDAO = accountDAO;
    }

    @GetMapping("/")
    public ResponseEntity<UserDetails> getUserDetails() {
        String username = authenticationService.getCurrentUsername();
        Optional<UserDetails> userDetails = accountDAO.getUserDetailsByUsername(username);
        return userDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
