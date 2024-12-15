package org.example.backend.dao;

import org.example.backend.entity.Driver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DriverDAO implements DAO<Driver, Long> {

    private JdbcTemplate jdbcTemplate;

    public DriverDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Driver> listAll() {
        return List.of();
    }

    @Override
    public void insert(Driver driver) {
        String sql =
                "INSERT INTO Driver(username, email, password, license_plate) VALUES(?,?,?,?)";

        jdbcTemplate.update(sql, driver.getUsername(),
                        driver.getEmail(), driver.getPassword(),
                        driver.getLicensePlate());
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
}
