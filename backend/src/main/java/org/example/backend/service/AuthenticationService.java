package org.example.backend.service;

import org.example.backend.dao.DriverDAO;
import org.example.backend.dto.DriverRegistrationDTO;
import org.example.backend.entity.Driver;
import org.example.backend.mapper.DriverRegistrationMapper;
import org.example.backend.security.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final DriverDAO driverDAO;
    private final DriverRegistrationMapper driverRegistrationMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(DriverDAO driverDAO,
                                 DriverRegistrationMapper driverRegistrationMapper,
                                 PasswordEncoder passwordEncoder,
                                 JWTService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.driverDAO = driverDAO;
        this.driverRegistrationMapper = driverRegistrationMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(DriverRegistrationDTO driverRegistrationDTO) {
        Driver driver = driverRegistrationMapper.fromDto(driverRegistrationDTO);
        encodePassword(driver);
        driverDAO.insert(driver);
    }

    private void encodePassword(Driver driver) {
        String encodedPassword = passwordEncoder.encode(driver.getAccount().getPassword());
        driver.getAccount().setPassword(encodedPassword);
    }

    public ResponseEntity<String> login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password
                ));

        ResponseEntity<String> response;
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(username);
            response = new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
