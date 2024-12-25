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

    public Integer countUsers() {
        String sql = "SELECT COUNT(*) FROM Account WHERE role_name = 'ROLE_USER'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Account> listAllUsers(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT username, role_name FROM Account WHERE role_name = 'ROLE_USER' LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> {
            Account account = new Account();
            account.setUsername(rs.getString("username"));
            account.setRole(rs.getString("role_name"));
            return account;
        });
    }
}
