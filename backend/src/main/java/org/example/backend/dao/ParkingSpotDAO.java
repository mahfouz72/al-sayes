package org.example.backend.dao;

import org.example.backend.entity.ParkingSpot;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ParkingSpotDAO implements DAO<ParkingSpot, Pair<Long, Long>> {
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
        String sql = "INSERT INTO ParkingSpot(id, lot_id, cost, " +
                     "current_status, type) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sql, parkingSpot.getId(),
                parkingSpot.getLotId(), parkingSpot.getCost(),
                parkingSpot.getCurrentStatus(), parkingSpot.getType());
    }

    @Override
    public Optional<ParkingSpot> getByPK(Pair<Long, Long> pKey) {
        String sql = "SELECT * FROM ParkingSpot WHERE id = ? AND lot_id = ?";
        Long spotId = pKey.getFirst();
        Long lotId = pKey.getSecond();
        ParkingSpot parkingSpot = null;
        try {
            parkingSpot = jdbcTemplate.queryForObject(sql, rowMapper, spotId, lotId);
        } catch(DataAccessException e) {
            // Not Found
        }
        return Optional.ofNullable(parkingSpot);
    }

    @Override
    public void update(Pair<Long, Long> pKey, ParkingSpot parkingSpot) {
        String sql = "UPDATE ParkingSpot SET lot_id = ?, cost = ?, " +
                "current_status = ?, type = ? WHERE id = ? AND lot_id = ?";
        Long spotId = pKey.getFirst();
        Long lotId = pKey.getSecond();
        jdbcTemplate.update(sql, parkingSpot.getLotId(), parkingSpot.getCost(),
                parkingSpot.getCurrentStatus(), parkingSpot.getType(), spotId, lotId);
    }

    @Override
    public void delete(Pair<Long, Long> pKey) {
        Long spotId = pKey.getFirst();
        Long lotId = pKey.getSecond();
        jdbcTemplate.update("DELETE FROM ParkingSpot WHERE id = ? " +
                "AND lot_id = ?", spotId, lotId);
    }

    public List<ParkingSpot> listAllSpotsFilterByLotId(long lot_id) {
        String sql = "SELECT * FROM ParkingSpot WHERE lot_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lot_id);
    }
}
