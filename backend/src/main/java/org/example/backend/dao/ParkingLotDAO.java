package org.example.backend.dao;

import org.example.backend.dto.ParkingLotDetails;
import org.example.backend.entity.ParkingLot;
import org.example.backend.entity.ParkingSpot;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ParkingLotDAO implements DAO<ParkingLot, Long> {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ParkingLot> rowMapper = (rs, rowNum) -> {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(rs.getLong("id"));
        parkingLot.setName(rs.getString("name"));
        parkingLot.setCapacity(rs.getInt("capacity"));
        parkingLot.setLocation(rs.getString("location"));
        parkingLot.setAutomaticReleaseTime(rs.getDouble("automatic_release_time"));
        parkingLot.setTimeLimit(rs.getDouble("time_limit"));
        parkingLot.setOverTimeScale(rs.getDouble("over_time_scale"));
        parkingLot.setNotShowingUpPenalty(rs.getDouble("not_showing_up_penalty"));
        return parkingLot;
    };

    public ParkingLotDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ParkingLot> listAll() {
        String sql = "SELECT * FROM ParkingLot";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void insert(ParkingLot parkingLot) {
    String sql = "INSERT INTO ParkingLot(name, managed_by, location, capacity, " +
                 "time_limit, automatic_release_time, not_showing_up_penalty, over_time_scale) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, parkingLot.getName(), parkingLot.getManagedBy(),
            parkingLot.getLocation(), parkingLot.getCapacity(), parkingLot.getTimeLimit(),
            parkingLot.getAutomaticReleaseTime(), parkingLot.getNotShowingUpPenalty(),
            parkingLot.getOverTimeScale());
    }

    @Override
    public Optional<ParkingLot> getByPK(Long id) {
        String sql = "SELECT * FROM ParkingLot WHERE id = ?";
        ParkingLot parkingLot = null;
        try {
            parkingLot = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch(DataAccessException e) {
            // Not Found
        }
        return Optional.ofNullable(parkingLot);
    }

    @Override
    public void update(Long id, ParkingLot parkingLot) {
        String sql = "UPDATE ParkingLot SET location = ?, capacity = ?, time_limit = ?, " +
                "automatic_release_time = ?, not_showing_up_penalty = ?, " +
                "over_time_scale = ? WHERE id = ?";
        jdbcTemplate.update(sql, parkingLot.getLocation(), parkingLot.getCapacity(),
                parkingLot.getTimeLimit(), parkingLot.getAutomaticReleaseTime(),
                parkingLot.getNotShowingUpPenalty(), parkingLot.getOverTimeScale(),
                id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM ParkingLot WHERE id = ?", id);
    }

public List<ParkingLotDetails> getLotsDetailsByManagerId(long managerId) {
    String sql = """
        SELECT 
            p.id AS lot_id,
            p.name AS lot_name,
            p.location,
            p.capacity,
            COUNT(CASE WHEN ps.current_status = 'OCCUPIED' THEN 1 END) AS occupied_count,
            SUM(CASE WHEN r.status IN ('ONGOING', 'FULFILLED', 'EXPIRED', 'CONFIRMED') THEN r.price ELSE 0 END) AS total_revenue,
            SUM(CASE WHEN r.violation_duration IS NOT NULL THEN r.penalty * r.violation_duration ELSE 0 END) AS total_violations
        FROM 
            ParkingLot p
        LEFT JOIN 
            ParkingSpot ps ON p.id = ps.lot_id
        LEFT JOIN
            Reservation r ON ps.id = r.spot_id
        WHERE 
            p.managed_by = ?
        GROUP BY 
            p.id, p.name, p.location, p.capacity
    """;

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
        ParkingLotDetails details = new ParkingLotDetails();
        details.setId(rs.getLong("lot_id"));
        details.setName(rs.getString("lot_name"));
        details.setLocation(rs.getString("location"));
        details.setCapacity(rs.getInt("capacity"));
        details.setRevenue(rs.getDouble("total_revenue"));
        details.setOccupancyRate(rs.getInt("occupied_count") / (double) details.getCapacity());
        details.setViolations(rs.getDouble("total_violations"));
        return details;
    }, managerId);
}
}
