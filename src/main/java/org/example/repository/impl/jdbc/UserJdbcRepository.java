package org.example.repository.impl.jdbc;
import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.IUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class UserJdbcRepository implements IUserRepository {

    private final DataSource dataSource;

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, login, password, role FROM users";

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading users", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

        return users;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getString("id"))
                .login(rs.getString(("login")))
                .password(rs.getString("password"))
                .role(rs.getString("role"))
                .build();
    }

    @Override
    public User save(User user) {

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            boolean exists = false;
            String checkSql = "SELECT id FROM users WHERE id = ?";

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, user.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }
            if (exists) {
                String updateSql = "UPDATE users SET login = ?, password = ?, role = ? WHERE id = ?";

                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {

                    updateStmt.setString(1, user.getLogin());
                    updateStmt.setString(2, user.getPassword());
                    updateStmt.setString(3, user.getRole());
                    updateStmt.setString(4, user.getId());

                    updateStmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO users (id, login, password, role) VALUES (?, ?, ?, ?)";

                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, user.getId());
                    insertStmt.setString(2, user.getLogin());
                    insertStmt.setString(3, user.getPassword());
                    insertStmt.setString(4, user.getRole());

                    insertStmt.executeUpdate();
                }
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT id, login, password, role FROM users WHERE login = ?";
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding user by login", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT id, login, password, role FROM users WHERE id = ?";
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding user by id", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return true;
    }
}
