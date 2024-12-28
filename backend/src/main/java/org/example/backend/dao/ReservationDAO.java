package org.example.backend.dao;

import org.example.backend.dto.ReservationDTO;
import org.example.backend.dto.ReservationDetailsDTO;
import org.example.backend.entity.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.example.backend.enums.ReservationStatus;
import org.example.backend.models.ReservationKey;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
// public key is composite key of driverId, spotId, lotId, startTime
public class ReservationDAO implements DAO<Reservation, ReservationKey> {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Reservation reservation = new Reservation();
        reservation.setDriverId(rs.getLong("driver_id"));
        reservation.setLotId(rs.getLong("lot_id"));
        reservation.setSpotId(rs.getLong("spot_id"));
        reservation.setStartTime(rs.getTimestamp("start_time"));
        reservation.setEndTime(rs.getTimestamp("end_time"));
        reservation.setPrice(rs.getDouble("price"));
        reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
        reservation.setViolationDuration(rs.getDouble("violation_duration"));
        reservation.setPenalty(rs.getDouble("penalty"));
        return reservation;
    };

    public ReservationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> listAll() {
        String sql = "SELECT * FROM Reservation";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Reservation> listAllStatus(ReservationStatus status) {
        String sql = "SELECT * FROM Reservation WHERE status = ?";
        return jdbcTemplate.query(sql, rowMapper, status.name());
    }

    @Override
    public void insert(Reservation reservation) {
        String sql = "INSERT INTO Reservation (driver_id, lot_id, spot_id, start_time, end_time, price, status, violation_duration, penalty) VALUES(?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, reservation.getDriverId(), reservation.getLotId(), reservation.getSpotId(),
                reservation.getStartTime(), reservation.getEndTime(), reservation.getPrice(),
                reservation.getStatus().name(), reservation.getViolationDuration(), reservation.getPenalty());
    }

    @Override
    public Optional<Reservation> getByPK(ReservationKey key) {
        String sql = "SELECT * FROM Reservation WHERE driver_id = ? AND lot_id = ? AND spot_id = ? AND start_time = ?";
        Reservation reservation = null;
        try {
            reservation = jdbcTemplate.queryForObject(sql, rowMapper, key.getDriverId(), key.getLotId(), key.getSpotId(),
                    key.getStartTime());
        } catch (Exception e) {
            // Not found
        }
        return Optional.ofNullable(reservation);
    }
    
    @Override
    public void update(ReservationKey key, Reservation reservation) {
        updateByKeys(key.getDriverId(), key.getLotId(), key.getSpotId(), key.getStartTime(), reservation);
    }

    public void updateByKeys(Long driverId, Long lotId, Long spotId, Timestamp startTime, Reservation reservation) {
        String sql = "UPDATE Reservation SET end_time = ?, price = ?, status = ?, violation_duration = ?, penalty = ? WHERE driver_id = ? AND lot_id = ? AND spot_id = ? AND start_time = ?";
        jdbcTemplate.update(sql, reservation.getEndTime(), reservation.getPrice(), reservation.getStatus().name(),
                reservation.getViolationDuration(), reservation.getPenalty(), driverId, lotId, spotId, startTime);
    }

    @Override
    public void delete(ReservationKey key) {
        deleteByKeys(key.getDriverId(), key.getLotId(), key.getSpotId(), key.getStartTime());
    }

    public void deleteByKeys(Long driverId, Long lotId, Long spotId, Timestamp startTime) {
        String sql = "DELETE FROM Reservation WHERE driver_id = ? AND lot_id = ? AND spot_id = ? AND start_time = ?";
        jdbcTemplate.update(sql, driverId, lotId, spotId, startTime);
    }

    public boolean isSpotAvailable(Long lotId, Long spotId, Timestamp startTime, Timestamp endTime) {
        // Check if the spot is available in the given time range (start_time <= startTime < end_time) or (start_time < endTime <= end_time) and the status is not CANCELLED
        // System.out.printf("Checking if spot %d in lot %d is available from %s to %s\n", spotId, lotId, startTime.toString(), endTime.toString());
        String sql = "SELECT * FROM Reservation WHERE lot_id = ? AND spot_id = ? AND ((start_time <= ? AND ? < end_time) OR (start_time < ? AND ? <= end_time)) AND status NOT IN (?, ?)";
        List<Reservation> reservations = jdbcTemplate.query(sql, rowMapper, lotId, spotId, startTime, startTime, endTime, endTime, ReservationStatus.EXPIRED.name(), ReservationStatus.FULFILLED.name());
        return reservations.isEmpty();
    }

    public List<ReservationDetailsDTO> getReservationsByLotManagerID(Long lotManagerId) {
        String sql = """
                   SELECT
                   	a.username,
                       a.email,
                       p.name,
                       CONCAT('S', e.spot_id) AS spot_id,
                       e.start_time,
                   	ROUND(
                   			TIMESTAMPDIFF(HOUR, e.start_time, e.end_time) +
                   			(TIMESTAMPDIFF(MINUTE, e.start_time, e.end_time) % 60) / 60.0,\s
                   			1) AS durations,
                       e.status,\s
                       e.price
                   FROM reservation e
                   JOIN parkinglot p
                   ON e.lot_id = p.id
                   JOIN account a
                   ON a.id = e.driver_id
                   WHERE p.managed_by = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReservationDetailsDTO reservationDetailsDTO = new ReservationDetailsDTO();
            reservationDetailsDTO.setDriverName(rs.getString("username"));
            reservationDetailsDTO.setDriverEmail(rs.getString("email"));
            reservationDetailsDTO.setParkingLot(rs.getString("name"));
            reservationDetailsDTO.setSpotNumber(rs.getString("spot_id"));
            reservationDetailsDTO.setStartTime(rs.getTimestamp("start_time").toString());
            reservationDetailsDTO.setDuration(rs.getDouble("durations"));
            reservationDetailsDTO.setStatus(rs.getString("status"));
            reservationDetailsDTO.setTotal(rs.getDouble("price"));
            return reservationDetailsDTO;
        }, lotManagerId);

    }

    public List<ReservationDetailsDTO> getAllReservations() {
         String sql = """
                   SELECT
                   	a.username,
                       a.email,
                       p.name,
                       CONCAT('S', e.spot_id) AS spot_id,
                       e.start_time,
                   	ROUND(
                   			TIMESTAMPDIFF(HOUR, e.start_time, e.end_time) +
                   			(TIMESTAMPDIFF(MINUTE, e.start_time, e.end_time) % 60) / 60.0,\s
                   			1) AS durations,
                       e.status,\s
                       e.price
                   FROM reservation e
                   JOIN parkinglot p
                   ON e.lot_id = p.id
                   JOIN account a
                   ON a.id = e.driver_id
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReservationDetailsDTO reservationDetailsDTO = new ReservationDetailsDTO();
            reservationDetailsDTO.setDriverName(rs.getString("username"));
            reservationDetailsDTO.setDriverEmail(rs.getString("email"));
            reservationDetailsDTO.setParkingLot(rs.getString("name"));
            reservationDetailsDTO.setSpotNumber(rs.getString("spot_id"));
            reservationDetailsDTO.setStartTime(rs.getTimestamp("start_time").toString());
            reservationDetailsDTO.setDuration(rs.getDouble("durations"));
            reservationDetailsDTO.setStatus(rs.getString("status"));
            reservationDetailsDTO.setTotal(rs.getDouble("price"));
            return reservationDetailsDTO;
        });
    }
  

    public List<ReservationDetailsDTO> getReservationsNearEndTime(long accountId) {
        String sql = """
                   SELECT
                   	p.name,
                   	CONCAT('S', e.spot_id) AS spot_id
                   FROM reservation e
                    JOIN parkinglot p ON e.lot_id = p.id
                    JOIN parkingspot s ON e.spot_id = s.id
                    WHERE
                   	    TIMESTAMPDIFF(MINUTE, e.end_time, NOW()) <= 30
                   	    AND status LIKE 'ONGOING'
                        AND driver_id =  ?;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReservationDetailsDTO reservationDetailsDTO = new ReservationDetailsDTO();
            reservationDetailsDTO.setSpotNumber(rs.getString("spot_id"));
            reservationDetailsDTO.setParkingLot(rs.getString("name"));
            return reservationDetailsDTO;
        }, accountId);
    }

    public List<ReservationDetailsDTO> getReservationsNearStartTime(long accountId) {
         String sql = """
                   SELECT
                   	p.name,
                   	CONCAT('S', e.spot_id) AS spot_id
                   FROM reservation e
                    JOIN parkinglot p ON e.lot_id = p.id
                    JOIN parkingspot s ON e.spot_id = s.id
                    WHERE
                   	    TIMESTAMPDIFF(MINUTE, e.start_time, NOW()) <= 30
                   	    AND status LIKE 'ONGOING'
                        AND driver_id =  ?;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReservationDetailsDTO reservationDetailsDTO = new ReservationDetailsDTO();
            reservationDetailsDTO.setSpotNumber(rs.getString("spot_id"));
            reservationDetailsDTO.setParkingLot(rs.getString("name"));
            return reservationDetailsDTO;
        }, accountId);
    }

public boolean reserveSpot(ReservationDTO reservationDTO) {
    Connection connection = null;
    CallableStatement callableStatement = null;
    
    try {
        // Get the database connection
        connection = jdbcTemplate.getDataSource().getConnection();
        
        // Prepare the stored procedure call
        String procedureCall = "{CALL ReserveParkingSpot(?, ?, ?, ?, ?, ?)}";
        callableStatement = connection.prepareCall(procedureCall);
        
        // Set the parameters for the stored procedure
        callableStatement.setLong(1, reservationDTO.getDriverId());  // p_driver_id
        callableStatement.setLong(2, reservationDTO.getSpotId());    // p_spot_id
        callableStatement.setLong(3, reservationDTO.getLotId());     // p_lot_id
        callableStatement.setTimestamp(4, reservationDTO.getStartTime()); // p_start_time
        callableStatement.setTimestamp(5, reservationDTO.getEndTime());   // p_end_time
        callableStatement.setBigDecimal(6, BigDecimal.valueOf(reservationDTO.getPrice()));       // p_price
        
        // Execute the stored procedure
        callableStatement.execute();
        
        return true;
    } catch (SQLException e) {
        // e.printStackTrace();
        return false;
    } finally {
        try {
            if (callableStatement != null) {
                callableStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            // ex.printStackTrace();
        }
    }
}
  
}
