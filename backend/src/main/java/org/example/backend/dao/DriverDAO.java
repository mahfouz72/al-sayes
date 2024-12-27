package org.example.backend.dao;

import org.example.backend.dto.DriverReservationDetailsDTO;
import org.example.backend.entity.Driver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DriverDAO implements DAO<Driver, Long> {

    private JdbcTemplate jdbcTemplate;
    private AccountDAO accountDAO;

    public DriverDAO(JdbcTemplate jdbcTemplate, AccountDAO accountDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDAO = accountDAO;
    }

    @Override
    public List<Driver> listAll() {
        return List.of();
    }

    @Override
    public void insert(Driver driver) {
        accountDAO.insert(driver.getAccount());

        String getAccountIdSql = "SELECT LAST_INSERT_ID()";
        Long accountId = jdbcTemplate.queryForObject(getAccountIdSql, Long.class);

        String DriverSql = "INSERT INTO Driver(account_id, license_plate) VALUES(?,?)";
        jdbcTemplate.update(DriverSql, accountId, driver.getLicensePlate());
    }

    @Override
    public Optional<Driver> getByPK(Long pKey) {
        return Optional.empty();
    }

    @Override
    public void update(Long pKey, Driver driver) {

    }

    @Override
    public void delete(Long pKey) {

    }

    public List<DriverReservationDetailsDTO> getReservations(long driverId) {
        String sql = """
                    SELECT p.name,
                        CONCAT('S', e.spot_id) AS spot_id,
                        e.start_time,
                        ROUND(
                                TIMESTAMPDIFF(HOUR, e.start_time, e.end_time) +
                                (TIMESTAMPDIFF(MINUTE, e.start_time, e.end_time) % 60) / 60.0,\s
                                1) AS durations,
                        e.status,
                        e.price
                    FROM reservation e
                    JOIN parkinglot p
                    ON e.lot_id = p.id
                    WHERE e.driver_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DriverReservationDetailsDTO driverReservationDetailsDTO = new DriverReservationDetailsDTO();
            driverReservationDetailsDTO.setParkingLot(rs.getString("name"));
            driverReservationDetailsDTO.setSpotNumber(rs.getString("spot_id"));
            driverReservationDetailsDTO.setStartTime(rs.getString("start_time"));
            driverReservationDetailsDTO.setDuration(rs.getDouble("durations"));
            driverReservationDetailsDTO.setStatus(rs.getString("status"));
            driverReservationDetailsDTO.setTotal(rs.getDouble("price"));
            return driverReservationDetailsDTO;
        }, driverId);
    }
}
