package org.example.backend.dao;

import org.example.backend.entity.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.example.backend.enums.ReservationStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationDAO implements DAO<Reservation> {
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

    public Optional<Reservation> getByDriverAndSpot(Long driverId, Long spotId, Long lotId) {
        String sql = "SELECT * FROM Reservation WHERE driver_id = ? AND spot_id = ? AND lot_id = ?";
        Reservation reservation = null;
        try {
            reservation = jdbcTemplate.queryForObject(sql, rowMapper, driverId, spotId, lotId);
        } catch (Exception e) {
            // Not found
        }
        return Optional.ofNullable(reservation);
    }

    @Override
    public void update(Long id, Reservation reservation) {
        throw new UnsupportedOperationException("Use composite keys for updating reservations.");
    }

    public void updateByKeys(Long driverId, Long spotId, Long lotId, Timestamp startTime, Reservation reservation) {
        String sql = "UPDATE Reservation SET end_time = ?, price = ?, status = ?, violation_duration = ?, penalty = ? WHERE driver_id = ? AND spot_id = ? AND lot_id = ? AND start_time = ?";
        jdbcTemplate.update(sql, reservation.getEndTime(), reservation.getPrice(), reservation.getStatus().name(),
                reservation.getViolationDuration(), reservation.getPenalty(), driverId, spotId, lotId, startTime);
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Use composite keys for deleting reservations.");
    }

    public void deleteByKeys(Long driverId, Long spotId, Long lotId, Timestamp startTime) {
        String sql = "DELETE FROM Reservation WHERE driver_id = ? AND spot_id = ? AND lot_id = ? AND start_time = ?";
        jdbcTemplate.update(sql, driverId, spotId, lotId, startTime);
    }
}
