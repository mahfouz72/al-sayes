package org.example.backend.dao;

import org.example.backend.dto.ParkingLotCard;
import org.example.backend.dto.ParkingLotDetails;
import org.example.backend.entity.ParkingLot;
import org.example.backend.entity.ParkingSpot;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        parkingLot.setLocation(rs.getString("location"));
        parkingLot.setLatitude(rs.getDouble("latitude"));
        parkingLot.setLongitude(rs.getDouble("longitude"));
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
    String sql = "INSERT INTO ParkingLot(name, managed_by, location, latitude, longitude, " +
                 "time_limit, automatic_release_time, not_showing_up_penalty, over_time_scale) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, parkingLot.getName(), parkingLot.getManagedBy(),
            parkingLot.getLocation(), parkingLot.getLatitude(), parkingLot.getLongitude(),
            parkingLot.getTimeLimit(),
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
        String sql = "UPDATE ParkingLot SET location = ?, latitude = ?, longitude = ?, " +
                "time_limit = ?, automatic_release_time = ?, not_showing_up_penalty = ?, " +
                "over_time_scale = ? WHERE id = ?";
        jdbcTemplate.update(sql, parkingLot.getLocation(), parkingLot.getLatitude(), parkingLot.getLongitude(),
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
                COUNT(DISTINCT ps.id) AS capacity,
                COUNT(DISTINCT CASE WHEN ps.current_status = 'OCCUPIED' THEN ps.id END) AS occupied_count,
                SUM(CASE WHEN r.status IN ('ONGOING', 'FULFILLED', 'EXPIRED', 'CONFIRMED') THEN r.price ELSE 0 END) AS total_revenue,
                SUM(CASE WHEN r.violation_duration IS NOT NULL THEN r.penalty * r.violation_duration ELSE 0 END) AS total_violations
            FROM
                ParkingLot p
            LEFT JOIN
                ParkingSpot ps ON p.id = ps.lot_id
            LEFT JOIN
                Reservation r ON ps.id = r.spot_id AND p.id = r.lot_id
            WHERE
                p.managed_by = ?
            GROUP BY
                p.id, p.name, p.location
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ParkingLotDetails details = new ParkingLotDetails();
            details.setId(rs.getLong("lot_id"));
            details.setName(rs.getString("lot_name"));
            details.setLocation(rs.getString("location"));
            details.setCapacity(rs.getInt("capacity"));
            details.setRevenue(rs.getDouble("total_revenue"));
            details.setOccupancyRate(rs.getInt("occupied_count") * 100.0 / details.getCapacity());
            details.setViolations(rs.getDouble("total_violations"));
            return details;
        }, managerId);
    }

    public Long insertAndReturnKey(ParkingLot parkingLot) {
        String sql = "INSERT INTO ParkingLot(name, managed_by, location, " +
             "time_limit, automatic_release_time, not_showing_up_penalty, over_time_scale) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Prepare the KeyHolder to capture the generated key
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Use the PreparedStatementCreator to capture the generated key
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, parkingLot.getName());
            ps.setLong(2, parkingLot.getManagedBy());
            ps.setString(3, parkingLot.getLocation());
            ps.setDouble(4, parkingLot.getTimeLimit());
            ps.setDouble(5, parkingLot.getAutomaticReleaseTime());
            ps.setDouble(6, parkingLot.getNotShowingUpPenalty());
            ps.setDouble(7, parkingLot.getOverTimeScale());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }
    public List<ParkingLotCard> getLotsCards() {
        String sql = """
            SELECT 
                p.id AS lot_id,
                p.name AS lot_name,
                p.location,
                p.latitude,
                p.longitude,
                IFNULL(ROUND(AVG(ps.cost), 2), 0) AS average_price,
                COUNT(ps.id) AS total_spots,
                COUNT(CASE WHEN ps.current_status = 'AVAILABLE' THEN 1 END) AS available_spots,
                COUNT(CASE WHEN ps.type = 'REGULAR' THEN 1 END) AS regular_spots,
                COUNT(CASE WHEN ps.type = 'DISABLED' THEN 1 END) AS disabled_spots,
                COUNT(CASE WHEN ps.type = 'EV_CHARGING' THEN 1 END) AS ev_spots
            FROM 
                ParkingLot p
            LEFT JOIN 
                ParkingSpot ps ON p.id = ps.lot_id
            GROUP BY 
                p.id, p.name, p.location
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ParkingLotCard card = new ParkingLotCard();
            card.setId(rs.getLong("lot_id"));
            card.setName(rs.getString("lot_name"));
            card.setLocation(rs.getString("location"));
            card.setLatitude(rs.getDouble("latitude"));
            card.setLongitude(rs.getDouble("longitude"));
            card.setAveragePrice(rs.getDouble("average_price"));
            card.setTotalSpots(rs.getInt("total_spots"));
            card.setAvailableSpots(rs.getInt("available_spots"));
            card.setRegularSpots(rs.getInt("regular_spots"));
            card.setDisabledSpots(rs.getInt("disabled_spots"));
            card.setEvSpots(rs.getInt("ev_spots"));
            return card;
        });
    }
}
