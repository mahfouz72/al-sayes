package org.example.backend.service;

import org.example.backend.dao.AccountDAO;
import org.example.backend.dao.DriverDAO;
import org.example.backend.dto.LoginResponseDTO;
import org.example.backend.dto.RegistrationDTO;
import org.example.backend.entity.Account;
import org.example.backend.entity.Driver;
import org.example.backend.mapper.RegistrationMapper;
import org.example.backend.security.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final DriverDAO driverDAO;
    private final AccountDAO accountDAO;
    private final RegistrationMapper registrationMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(DriverDAO driverDAO, AccountDAO accountDAO,
                                 RegistrationMapper registrationMapper,
                                 PasswordEncoder passwordEncoder,
                                 JWTService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.driverDAO = driverDAO;
        this.accountDAO = accountDAO;
        this.registrationMapper = registrationMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegistrationDTO registrationDTO) {
        if ("Driver".equalsIgnoreCase(registrationDTO.role())) {
            registerDriver(registrationDTO);
        } else if ("Manager".equalsIgnoreCase(registrationDTO.role())) {
            registerManager(registrationDTO);
        }
    }

    private void registerDriver(RegistrationDTO registrationDTO) {
        Account account = registrationMapper.accountFromDto(registrationDTO);
        encodePassword(account);
        Driver driver = Driver.builder()
                .account(account)
                .licensePlate(registrationDTO.licensePlate())
                .paymentMethod(registrationDTO.paymentMethod())
                .build();
        driverDAO.insert(driver);
    }

    private void registerManager(RegistrationDTO registrationDTO) {
        Account account = registrationMapper.accountFromDto(registrationDTO);
        encodePassword(account);
        accountDAO.insert(account);
    }

    private void encodePassword(Account account) {
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
    }

    public ResponseEntity<LoginResponseDTO> login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password
                ));

        ResponseEntity<LoginResponseDTO> response;
        if (authentication.isAuthenticated()) {
            LoginResponseDTO loginResponse = prepareResponse(username);
            response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    private LoginResponseDTO prepareResponse(String username) {
        String token = jwtService.generateToken(username);
        String role = accountDAO.getRoleByUsername(username).toLowerCase().split("_")[1];
        return LoginResponseDTO.builder()
                .role(role)
                .token(token)
                .build();
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    public Optional<Account> getCurrentAccount() {
        return accountDAO.getByUsername(getCurrentUsername());
    }
}
