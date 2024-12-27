package org.example.backend.service;

import org.example.backend.dao.ParkingLotDAO;
import org.example.backend.dao.ParkingSpotDAO;
import org.example.backend.dao.ReservationDAO;
import org.example.backend.dto.ReservationDTO;
import org.example.backend.entity.ParkingLot;
import org.example.backend.entity.ParkingSpot;
import org.example.backend.entity.Reservation;
import org.example.backend.enums.ReservationStatus;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final ParkingSpotDAO parkingSpotDAO;
    private final ParkingLotDAO parkingLotDAO;

    public ReservationService(ReservationDAO reservationDAO, ParkingSpotDAO parkingSpotDAO,
            ParkingLotDAO parkingLotDAO) {
        this.reservationDAO = reservationDAO;
        this.parkingSpotDAO = parkingSpotDAO;
        this.parkingLotDAO = parkingLotDAO;
    }

    public boolean reserveSpot(ReservationDTO reservationDTO) {
        Optional<ParkingSpot> spot = parkingSpotDAO.getByPK(Pair.of(reservationDTO.getSpotId(), reservationDTO.getLotId()));
        if (spot.isPresent()) {
            parkingSpotDAO.update(Pair.of(reservationDTO.getSpotId(), reservationDTO.getLotId()),
                    new ParkingSpot(reservationDTO.getSpotId(), reservationDTO.getLotId(), spot.get().getCost(),
                            "RESERVED", spot.get().getType()));
            reservationDAO.insert(new Reservation(reservationDTO.getDriverId(), reservationDTO.getLotId(),
                    reservationDTO.getSpotId(), reservationDTO.getStartTime(), reservationDTO.getEndTime(),
                    reservationDTO.getPrice(), reservationDTO.getStatus(), reservationDTO.getViolationDuration(),
                    reservationDTO.getPenalty()));
            return true;
        }
        return false;
    }

    public boolean reserveSpot(Long driverId, Long lotId, Long spotId, Timestamp startTime, Timestamp endTime) {
        Optional<ParkingSpot> spot = parkingSpotDAO.getByPK(Pair.of(spotId, lotId));

        if (spot.isPresent() && "AVAILABLE".equalsIgnoreCase(spot.get().getCurrentStatus())) {
            // double price = calculatePrice(spot.get(), startTime, endTime);
            double price = 0.0;
            Reservation reservation = new Reservation(driverId, lotId, spotId, startTime,
                    endTime, price, ReservationStatus.PENDING, 0, 0);

            reservationDAO.insert(reservation);
            parkingSpotDAO.update(Pair.of(spotId, lotId),
                    new ParkingSpot(spotId, lotId, spot.get().getCost(), "RESERVED", spot.get().getType()));
            return true;
        }
        return false;
    }

    public void releaseExpiredReservations() {
        List<Reservation> reservations = reservationDAO.listAllStatus(ReservationStatus.CONFIRMED);
        for (Reservation res : reservations) {
            LocalDateTime now = LocalDateTime.now();
            double autoRelease = parkingLotDAO.getByPK(res.getLotId()).map(ParkingLot::getAutomaticReleaseTime)
                    .orElse(0.0);
            LocalDateTime releaseTime = res.getStartTime().toLocalDateTime().plusSeconds((long) autoRelease * 60);
            // System.out.println("Now: " + now);
            // System.out.println("Start Time: " + res.getStartTime().toLocalDateTime());
            // System.out.println("Auto Release: " + autoRelease + " minutes");
            // System.out.println("Release Time: " + releaseTime);
            if (now.isAfter(releaseTime)) {
                // System.out.println("Releasing reservation: " + res);
                reservationDAO.updateByKeys(res.getDriverId(), res.getLotId(), res.getSpotId(), res.getStartTime(),
                        new Reservation(res.getDriverId(), res.getLotId(), res.getSpotId(), res.getStartTime(),
                                res.getEndTime(), res.getPrice(), ReservationStatus.EXPIRED, 0, 0));
                // TODO: Not Showing Up Penalty
            }

        }
    }

    public void checkPenalties() {
        List<Reservation> reservations = reservationDAO.listAllStatus(ReservationStatus.ONGOING);
        for (Reservation res : reservations) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(res.getEndTime().toLocalDateTime())) {
                Duration extraTime = Duration.between(res.getEndTime().toLocalDateTime(), now);
                Optional<ParkingSpot> spot = parkingSpotDAO.getByPK(Pair.of(res.getSpotId(), res.getLotId()));
                Optional<ParkingLot> lot = parkingLotDAO.getByPK(res.getLotId());
                double pricePerHour = spot.map(ParkingSpot::getCost).orElse(0.0)
                        * lot.map(ParkingLot::getOverTimeScale).orElse(0.0);
                double penalty = extraTime.toSeconds() / 3600.0 * pricePerHour;
                reservationDAO.updateByKeys(res.getDriverId(), res.getSpotId(), res.getLotId(), res.getStartTime(),
                        new Reservation(res.getDriverId(), res.getLotId(), res.getSpotId(), res.getStartTime(),
                                Timestamp.valueOf(now), res.getPrice() + penalty, ReservationStatus.ONGOING,
                                extraTime.toHours(), penalty));
            }
        }
    }
}
