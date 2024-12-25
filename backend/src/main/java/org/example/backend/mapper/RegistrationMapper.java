package org.example.backend.mapper;

import org.example.backend.dto.RegistrationDTO;
import org.example.backend.entity.Account;
import org.example.backend.entity.Driver;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMapper {
    public Account accountFromDto(RegistrationDTO registrationDTO) {
        return Account.builder()
                .username(registrationDTO.username())
                .email(registrationDTO.email())
                .password(registrationDTO.password())
                .role(registrationDTO.role())
                .build();
    }
}