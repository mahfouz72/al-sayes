package org.example.backend.dao;

import org.example.backend.entity.ParkingLot;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component

public class ParkingLotDAO implements DAO<ParkingLot, Long> {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<ParkingLot> rowMapper = (rs, rowNum) -> {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(rs.getLong("id"));
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
        String sql = "INSERT TO TABLE ParkingLot(location, capacity, time_limit, " +
                "automatic_release_time, not_showing_up_penalty, " +
                "over_time_scale) VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, parkingLot.getLocation(), parkingLot.getCapacity(),
                parkingLot.getTimeLimit(), parkingLot.getAutomaticReleaseTime(),
                parkingLot.getNotShowingUpPenalty(), parkingLot.getOverTimeScale());
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
}
