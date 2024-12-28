package org.example.backend.dao;

import org.example.backend.dto.ParkingLotCard;
import org.example.backend.dto.ParkingLotDetails;
import org.example.backend.dto.ParkingLotStatisticsDTO;
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
    
    private RowMapper<ParkingLotStatisticsDTO> statisticsRowMapper = (rs, rowNum) -> {
        ParkingLotStatisticsDTO statistics = new ParkingLotStatisticsDTO();
        statistics.setLotId(rs.getLong("lot_id"));
        statistics.setTotalSpots(rs.getInt("capacity"));
        statistics.setOccupiedSpots(rs.getInt("occupied_count"));
        statistics.setAvailableSpots(statistics.getTotalSpots() - statistics.getOccupiedSpots());
        statistics.setAvgPrice(rs.getDouble("average_price"));
        statistics.setRegularSpots(rs.getInt("regular_spots"));
        statistics.setDisabledSpots(rs.getInt("disabled_spots"));
        statistics.setEvSpots(rs.getInt("ev_spots"));
        statistics.setReservations(rs.getInt("reservations"));
        statistics.setTotalRevenue(rs.getDouble("total_revenue"));
        statistics.setTotalViolations(rs.getInt("total_violations"));
        return statistics;
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
        String sql = "UPDATE ParkingLot SET name = ?, location = ?, latitude = ?, longitude = ?, " +
                "time_limit = ?, automatic_release_time = ?, not_showing_up_penalty = ?, " +
                "over_time_scale = ? WHERE id = ?";
        jdbcTemplate.update(sql, parkingLot.getName(), parkingLot.getLocation(), parkingLot.getLatitude(),
                parkingLot.getLongitude(), parkingLot.getTimeLimit(), parkingLot.getAutomaticReleaseTime(),
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
                p.location AS location,
                p.latitude,
                p.longitude,
                s.total_spots AS capacity,
                s.occupied_spots AS occupied_count,
                s.total_revenue AS total_revenue,
                s.total_violations AS total_violations
            FROM 
                ParkingLot p
            JOIN 
                Statistics s ON s.lot_id = p.id
            WHERE
                p.managed_by = ?;
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ParkingLotDetails details = new ParkingLotDetails();
            details.setId(rs.getLong("lot_id"));
            details.setName(rs.getString("lot_name"));
            details.setLocation(rs.getString("location"));
            details.setLatitude(rs.getDouble("latitude"));
            details.setLongitude(rs.getDouble("longitude"));
            details.setCapacity(rs.getInt("capacity"));
            details.setRevenue(rs.getDouble("total_revenue"));
            details.setOccupancyRate(details.getCapacity() == 0 ? 0 : (double) rs.getInt("occupied_count") * 100 / details.getCapacity());
            details.setViolations(rs.getDouble("total_violations"));
            return details;
        }, managerId);
    }

    public Long insertAndReturnKey(ParkingLot parkingLot) {
        String sql = "INSERT INTO ParkingLot(name, managed_by, location, latitude, longitude, " +
             "time_limit, automatic_release_time, not_showing_up_penalty, over_time_scale) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Prepare the KeyHolder to capture the generated key
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Use the PreparedStatementCreator to capture the generated key
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, parkingLot.getName());
            ps.setLong(2, parkingLot.getManagedBy());
            ps.setString(3, parkingLot.getLocation());
            ps.setDouble(4, parkingLot.getLatitude());
            ps.setDouble(5, parkingLot.getLongitude());
            ps.setDouble(6, parkingLot.getTimeLimit());
            ps.setDouble(7, parkingLot.getAutomaticReleaseTime());
            ps.setDouble(8, parkingLot.getNotShowingUpPenalty());
            ps.setDouble(9, parkingLot.getOverTimeScale());
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
                s.avg_price AS average_price,
                s.total_spots,
                s.available_spots,
                s.regular_spots,
                s.disabled_spots,
                s.ev_spots
            FROM
                ParkingLot p
            JOIN
                Statistics s ON p.id = s.lot_id
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

    public String getParkingLotNameById(Long lotId) {
        String sql = "SELECT name FROM ParkingLot WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, lotId);
    }
}
