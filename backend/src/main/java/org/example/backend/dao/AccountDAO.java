package org.example.backend.dao;

import org.example.backend.dto.UserDetails;
import org.example.backend.entity.Account;
import org.example.backend.entity.ParkingLot;
import org.example.backend.enums.PaymentMethod;
import org.example.backend.enums.UserStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountDAO implements DAO<Account, Long>  {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Account> rowMapper = (rs, rowNum) -> {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setUsername(rs.getString("username"));
        account.setEmail(rs.getString("email"));
        account.setPassword(rs.getString("password"));
        account.setRole(rs.getString("role_name"));
        account.setStatus(UserStatus.valueOf(rs.getString("status")));
        return account;
    };

    public AccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> listAll() {
        String sql = "SELECT * FROM Account";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void insert(Account account) {
        String accountSql =
                "INSERT INTO Account(username, email, password, role_name, status) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(accountSql, account.getUsername(),
                account.getEmail(), account.getPassword(), "ROLE_" + account.getRole().toUpperCase(),
                UserStatus.ACTIVE.name());
    }

    public String getRoleByUsername(String username) {
        String roleSql = "SELECT role_name FROM Account WHERE username = ?";
        return jdbcTemplate.queryForObject(roleSql, String.class, username);
    }

    @Override
    public Optional<Account> getByPK(Long id) {
        String sql = "SELECT * FROM Account WHERE id = ?";
        Account account = null;
        try {
            account = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch(DataAccessException e) {
            // Not Found
        }
        return Optional.ofNullable(account);
    }

    @Override
    public void update(Long id, Account account) {
        String sql = "UPDATE Account SET username = ?, email = ?, password = ?, role_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, account.getUsername(), account.getEmail(), account.getPassword(),
                "ROLE_" + account.getRole().toUpperCase(), id);
    }

    @Override
    public void delete(Long pKey) {
        String sql = "DELETE FROM Account WHERE id = ?";
        jdbcTemplate.update(sql, pKey);
    }

    public Optional<Account> getByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        Account account = null;
        try {
            account = jdbcTemplate.queryForObject(sql, rowMapper, username);
        } catch(DataAccessException e) {
            // Not Found
        }
        return Optional.ofNullable(account);
    }

    public Optional<UserDetails> getUserDetailsByUsername(String username) {
        String sql = """
                SELECT 
                    a.id,
                    a.username,
                    a.email,
                    a.role_name,
                    d.license_plate,
                    d.payment_method
                FROM Account a
                LEFT JOIN Driver d ON a.id = d.account_id
                WHERE a.username = ?;
                """;
        UserDetails userDetails = null;
        try {
            userDetails = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                UserDetails details = new UserDetails();
                details.setId(rs.getLong("id"));
                details.setUsername(rs.getString("username"));
                details.setEmail(rs.getString("email"));
                details.setRole(rs.getString("role_name").split("_")[1].toLowerCase());
                // Driver details if exists
                if (rs.getString("license_plate") != null) {
                    details.setLicensePlate(rs.getString("license_plate"));
                    details.setPaymentMethod(rs.getString("payment_method").toLowerCase());
                }
                return details;
            }, username);
        } catch(DataAccessException e) {
            // Not Found
        }
        return Optional.ofNullable(userDetails);
    }

    public void blockUser(String username) {
        String sql = "UPDATE Account SET status = ? WHERE username = ?";
        jdbcTemplate.update(sql, UserStatus.BLOCKED.name(), username);
    }

    public void unblockUser(String username) {
        String sql = "UPDATE Account SET status = ? WHERE username = ?";
        jdbcTemplate.update(sql, UserStatus.ACTIVE.name(), username);
    }

    public boolean isActive(String username) {
        String sql = "SELECT status FROM Account WHERE username = ?";
        return UserStatus.ACTIVE.name().equals(jdbcTemplate.queryForObject(sql, String.class, username));
    }
}
