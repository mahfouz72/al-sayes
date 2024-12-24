package org.example.backend.mapper;

import org.example.backend.dto.DriverRegistrationDTO;
import org.example.backend.entity.Account;
import org.example.backend.entity.Driver;
import org.springframework.stereotype.Service;

@Service
public class DriverRegistrationMapper {
    public Driver fromDto(DriverRegistrationDTO driverRegistrationDTO) {
        Account account = Account.builder()
                .username(driverRegistrationDTO.username())
                .email(driverRegistrationDTO.email())
                .password(driverRegistrationDTO.password())
                .build();

        return Driver.builder()
                .licensePlate(driverRegistrationDTO.licensePlate())
                .account(account)
                .build();
    }
}