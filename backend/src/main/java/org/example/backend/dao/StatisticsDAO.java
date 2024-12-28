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
        String sql = "SELECT username, role_name, status FROM Account LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> {
            UserDetailsDTO account = new UserDetailsDTO();
            account.setUsername(rs.getString("username"));
            account.setRole(rs.getString("role_name"));
            account.setStatus(rs.getString("status"));
            return account;
        });
    }

    public Integer countParkingLots() {
        String sql = "SELECT COUNT(*) FROM ParkingLot";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getTotalRevenue() {
        String sql = """
                SELECT
                    COALESCE(SUM(price), 0)
                FROM
                    Reservation
                WHERE
                    status != 'EXPIRED';
            """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getTotalViolations() {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE violation_duration > 0";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getMonthlyRevenue() {
        // revenue from now() - 30 days
        String sql = """
                SELECT
                    COALESCE(SUM(price), 0)
                FROM
                    Reservation
                WHERE
                    start_time >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) AND status != 'EXPIRED';
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public Integer getMonthlyRevenue(int month) {
        String sql = """
                SELECT
                    COALESCE(SUM(price), 0)
                FROM Reservation
                WHERE 
                    MONTH(start_time) = ? AND status != 'EXPIRED';
            """;
        return jdbcTemplate.queryForObject(sql, Integer.class, month);
    }

    public List<Map<String, Object>> getParkingSlotsWithRevenueAndOccupancy(int limit) {
        String sql = """
                SELECT
                p.name,
                s.total_revenue,
                IFNULL(ROUND(s.occupied_spots * 100.0 / s.total_spots, 2), 0) AS occupancy_rate
                FROM ParkingLot p
                JOIN Statistics s ON p.id = s.lot_id
                ORDER BY s.total_revenue DESC
                LIMIT ?;
            """;
        return jdbcTemplate.queryForList(sql, limit);
    }

    public List<Map<String, Object>> getTopUsersWithMostReservations(int limit) {
        String sql = """
                SELECT
                    a.username, email,
                    COUNT(*) AS total_reservations,
                    COALESCE(SUM(r.price), 0) AS total_spent
                FROM Reservation r
                JOIN Account a ON r.driver_id = a.id
                WHERE r.status != 'EXPIRED'
                GROUP BY a.id
                ORDER BY total_reservations DESC, total_spent DESC
                LIMIT ?;
            """;
        return jdbcTemplate.queryForList(sql, limit);
    }

    public List<Map<String, Object>> getDailyRevenue(int limit) {
        String sql = """
                SELECT * FROM (
                    SELECT DATE_FORMAT(start_time, '%Y-%m-%d') AS date, 
                        COALESCE(SUM(price), 0) AS revenue
                    FROM Reservation
                    WHERE status != 'EXPIRED'
                    GROUP BY DATE_FORMAT(start_time, '%Y-%m-%d')
                    ORDER BY date DESC
                    LIMIT ?
                ) AS temp
                ORDER BY date ASC;
        """;
        return jdbcTemplate.queryForList(sql, limit);
    }

    public List<Map<String, Object>> getDailyReservedSpots(int limit) {
        String sql = """
            SELECT * FROM (
                SELECT 
                    DATE_FORMAT(start_time, '%Y-%m-%d') AS date, 
                    COUNT(*) AS reserved_spots
                FROM Reservation
                WHERE status != 'EXPIRED'
                GROUP BY date
                ORDER BY date DESC
                LIMIT ?
            ) AS temp
            ORDER BY date ASC
        """;
        return jdbcTemplate.queryForList(sql, limit);
    }
}
