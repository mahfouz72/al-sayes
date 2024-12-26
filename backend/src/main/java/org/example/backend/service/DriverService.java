package org.example.backend.service;

import org.example.backend.dao.DriverDAO;
import org.example.backend.dto.DriverReservationDetailsDTO;
import org.example.backend.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final AuthenticationService authenticationService;
    private final DriverDAO driverDAO;

    public DriverService(AuthenticationService authenticationService, DriverDAO driverDAO) {
        this.authenticationService = authenticationService;
        this.driverDAO = driverDAO;
    }


    public List<DriverReservationDetailsDTO> getReservations() {
        Optional<Account> driver = authenticationService.getCurrentAccount();
        List<DriverReservationDetailsDTO> driverReservations = null;
        if (driver.isPresent()) {
            long DriverId = driver.get().getId();
            driverReservations =  driverDAO.getReservations(DriverId);
        }
        return driverReservations;
    }
}
