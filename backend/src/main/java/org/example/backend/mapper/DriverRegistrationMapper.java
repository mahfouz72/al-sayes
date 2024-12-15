package org.example.backend.mapper;

import org.example.backend.dto.DriverRegistrationDTO;
import org.example.backend.entity.Driver;
import org.springframework.stereotype.Service;

@Service
public class DriverRegistrationMapper {
    public Driver fromDto(DriverRegistrationDTO driverRegistrationDTO) {
        return Driver.builder()
                .username(driverRegistrationDTO.username())
                .email(driverRegistrationDTO.email())
                .password(driverRegistrationDTO.password())
                .licensePlate(driverRegistrationDTO.licensePlate())
                .build();
    }
}