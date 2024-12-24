package org.example.backend.dao;

import org.example.backend.entity.Account;
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
        Account account = driver.getAccount();
        String AccountSql =
                "INSERT INTO Account(username, email, password, role_name) VALUES(?,?,?,?)";
        jdbcTemplate.update(AccountSql, account.getUsername(),
                account.getEmail(), account.getPassword(), "ROLE_USER");

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
}
