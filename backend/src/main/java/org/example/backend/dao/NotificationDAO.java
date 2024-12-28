package org.example.backend.dao;

import org.example.backend.dto.NotificationDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationDAO implements DAO<NotificationDTO, Long>  {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<NotificationDTO> rowMapper = (rs, rowNum) -> {
        NotificationDTO notification = new NotificationDTO();
        notification.setId(rs.getLong("id"));
        notification.setMessage(rs.getString("message"));
        notification.setAccountId(rs.getLong("account_id"));
        notification.setTimestamp(rs.getTimestamp("timestamp"));
        return notification;
    };

    public NotificationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<NotificationDTO> listAll() {
        String sql = "SELECT * FROM Notification";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void insert(NotificationDTO notification) {
        String notificationSql =
                "INSERT INTO Notification(message, account_id, timestamp) VALUES(?,?,?)";
        jdbcTemplate.update(notificationSql, notification.getMessage(),
                notification.getAccountId(), notification.getTimestamp());
    }

    @Override
    public Optional<NotificationDTO> getByPK(Long id) {
        String sql = "SELECT * FROM Notification WHERE id = ?";
        NotificationDTO notification = null;
        try {
            notification = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException e) {
            // log exception
        }
        return Optional.ofNullable(notification);
    }

    public List<NotificationDTO> getNotificationsByAccountId(Long accountId) {
        String sql = "SELECT * FROM Notification WHERE account_id = ?";
        return jdbcTemplate.query(sql, rowMapper, accountId);
    }

    public void deleteNotificationByAccountId(Long accountId) {
        String sql = "DELETE FROM Notification WHERE account_id = ?";
        jdbcTemplate.update(sql, accountId);
    }

    @Override
    public void update(Long pKey, NotificationDTO t) {
        String sql = "UPDATE Notification SET message = ?, account_id = ?, timestamp = ? WHERE id = ?";
        jdbcTemplate.update(sql, t.getMessage(), t.getAccountId(), t.getTimestamp(), pKey);
    }

    @Override
    public void delete(Long pKey) {
        String sql = "DELETE FROM Notification WHERE id = ?";
        jdbcTemplate.update(sql, pKey);
    }
}
