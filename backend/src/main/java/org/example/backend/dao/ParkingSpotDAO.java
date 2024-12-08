package org.example.backend.dao;

import org.example.backend.entity.ParkingSpot;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ParkingSpotDAO implements DAO<ParkingSpot> {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<ParkingSpot> rowMapper = (rs, rowNum) -> {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setId(rs.getLong("id"));
        parkingSpot.setLotId(rs.getLong("lot_id"));
        parkingSpot.setCost(rs.getDouble("cost"));
        parkingSpot.setCurrentStatus(rs.getString("current_status"));
        parkingSpot.setType(rs.getString("type"));
        return parkingSpot;
    };

    public ParkingSpotDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ParkingSpot> listAll() {
        String sql = "SELECT * FROM ParkingSpot";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void insert(ParkingSpot parkingSpot) {
        String sql = "INSERT TO TABLE ParkingSpot(lot_id, cost, " +
                "current_status, type) VALUES(?,?,?,?)";
        jdbcTemplate.update(sql, parkingSpot.getLotId(), parkingSpot.getCost(),
                parkingSpot.getCurrentStatus(), parkingSpot.getType());
    }

    @Override
    public Optional<ParkingSpot> getById(Long id) {
        String sql = "SELECT * FROM ParkingSpot WHERE id = ?";
        ParkingSpot parkingSpot = null;
        try {
            parkingSpot = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch(DataAccessException e) {
            // Not Found
        }
        return Optional.ofNullable(parkingSpot);
    }

    @Override
    public void update(Long id, ParkingSpot parkingSpot) {
        String sql = "UPDATE ParkingSpot SET lot_id = ?, cost = ?, " +
                "current_status = ?, type = ? WHERE id = ?";
        jdbcTemplate.update(sql, parkingSpot.getLotId(), parkingSpot.getCost(),
                parkingSpot.getCurrentStatus(), parkingSpot.getType(), id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM ParkingSpot WHERE id = ?", id);
    }
}
