package org.example.backend.dao;

import org.example.backend.entity.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    public Integer getTotalRevenue() {
        String sql = "SELECT SUM(price) FROM Reservation WHERE status = 'PAID'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getTotalViolations() {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE status = 'VIOLATED'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getMonthlyRevenue() {
        String sql = "SELECT SUM(price) FROM Reservation WHERE MONTH(start_time) = MONTH(CURRENT_DATE())";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public Integer getMonthlyRevenue(int month) {
        String sql = "SELECT SUM(price) FROM Reservation WHERE MONTH(start_time) = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, month);
    }

    public List<Map<String, Object>> getParkingSlotsWithRevenueAndOccupancy(int limit) {
        String sql = "SELECT name, SUM(price) AS total_revenue, SUM(0) AS occupancy_rate " +
                     "FROM ParkingLot JOIN Reservation ON ParkingLot.id = lot_id " +
                     "GROUP BY lot_id ORDER BY total_revenue DESC LIMIT ?";
        return jdbcTemplate.queryForList(sql, limit);
    }

    public List<Map<String, Object>> getTopUsersWithMostReservations(int limit) {
        String sql = "SELECT username, COUNT(*) as total_reservations, SUM(price) as total_revenue, 0 " +
                     "FROM Reservation JOIN Account ON driver_id = id " +
                     "GROUP BY driver_id ORDER BY total_reservations DESC LIMIT ?";
        return jdbcTemplate.queryForList(sql, limit);
    }

}
