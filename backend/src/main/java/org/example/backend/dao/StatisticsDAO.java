package org.example.backend.dao;

import org.example.backend.dto.UserDetailsDTO;
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
        String sql = "SELECT COUNT(*) FROM Account";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer countManagers() {
        String sql = "SELECT COUNT(*) FROM Account WHERE role_name = 'ROLE_MANAGER'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer countDrivers() {
        String sql = "SELECT COUNT(*) FROM Driver";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<UserDetailsDTO> listAllUsers(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT username, role_name FROM Account LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> {
            UserDetailsDTO account = new UserDetailsDTO();
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
        String sql = "SELECT COALESCE(SUM(price + penalty), 0) FROM Reservation";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getTotalViolations() {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE violation_duration > 0";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getMonthlyRevenue() {
        String sql = "SELECT COALESCE(SUM(price + penalty), 0) FROM Reservation WHERE MONTH(start_time) = MONTH(CURRENT_DATE())";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public Integer getMonthlyRevenue(int month) {
        String sql = "SELECT COALESCE(SUM(price + penalty), 0) FROM Reservation WHERE MONTH(start_time) = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, month);
    }

    public List<Map<String, Object>> getParkingSlotsWithRevenueAndOccupancy(int limit) {
        String sql = """
                     SELECT name,
                     COALESCE(SUM(price+penalty), 0) AS total_revenue,
                     COUNT(DISTINCT CASE WHEN ps.current_status = 'OCCUPIED' THEN ps.id END) * 100.0 / COUNT(DISTINCT ps.id) AS occupancy_rate
                     FROM ParkingLot p
                     JOIN Reservation r ON p.id = r.lot_id
                     JOIN ParkingSpot ps ON ps.lot_id = p.id
                     GROUP BY r.lot_id
                     ORDER BY total_revenue DESC
                     LIMIT ?;
        """;
        return jdbcTemplate.queryForList(sql, limit);
    }

    public List<Map<String, Object>> getTopUsersWithMostReservations(int limit) {
        String sql = "SELECT username, COUNT(*) as total_reservations, COALESCE(SUM(price+penalty), 0) as total_spent " +
                     "FROM Reservation JOIN Account ON driver_id = id " +
                     "GROUP BY driver_id ORDER BY total_reservations, total_spent DESC LIMIT ?";
        return jdbcTemplate.queryForList(sql, limit);
    }

    public List<Map<String, Object>> getDailyRevenue(int limit) {
        String sql = """
                SELECT * FROM (
                SELECT DATE_FORMAT(start_time, '%Y-%m-%d') as date, 
                       COALESCE(SUM(price + penalty), 0) as revenue
                FROM Reservation GROUP BY date ORDER BY date DESC LIMIT ?
                ) AS temp
                ORDER BY date ASC
        """;
        return jdbcTemplate.queryForList(sql, limit);
    }

    public List<Map<String, Object>> getDailyReservedSpots(int limit) {
        String sql = """
                SELECT * FROM (
                SELECT DATE_FORMAT(start_time, '%Y-%m-%d') as date, 
                       COUNT(*) as reserved_spots
                FROM Reservation GROUP BY date ORDER BY date DESC LIMIT ?
                ) AS temp
                ORDER BY date ASC
        """;
        return jdbcTemplate.queryForList(sql, limit);
    }



}
