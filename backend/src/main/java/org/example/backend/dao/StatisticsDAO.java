package org.example.backend.dao;

import org.example.backend.entity.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatisticsDAO {

    private JdbcTemplate jdbcTemplate;

    public StatisticsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public Integer countParkingLots() {
        String sql = "SELECT COUNT(*) FROM ParkingLot";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


}
