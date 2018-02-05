package com.miniseva.security.social.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AccountRepositoryJDBCProvider implements AccountRepositoryJDBC {

    private final JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    @Inject
    public AccountRepositoryJDBCProvider(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createAccount(Account user) throws UsernameAlreadyInUseException {
        try {
            jdbcTemplate
                    .update("INSERT INTO account (first_name, last_name, username, email, password, enabled) VALUES (?, ?, ?, ?, ?, ?)",
                            user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(),
                            passwordEncoder.encode(user.getPassword()), user.isEnabled());
        } catch (DuplicateKeyException e) {
            throw new UsernameAlreadyInUseException(user.getUsername());
        }
    }

    public Account findAccountByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT first_name, last_name, username, email, enabled FROM account WHERE username = ?",
                new RowMapper<Account>() {
                    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Account(rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getBoolean("enabled"));
                    }
                }, username);
    }

}
