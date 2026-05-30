package org.example.repository;

import org.example.JdbcConnectionManager;
import org.example.model.Rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalJdbcRepository implements IRentalRepository {
    IUserRepository userRepo;
    IVehicleRepository vehicleRepo;
    public RentalJdbcRepository(IUserRepository userRepo, IVehicleRepository vehicleRepo) {
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
    }

    private final java.text.SimpleDateFormat isoFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public List<Rental> findAll() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental";

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US);

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading rentals", e);
        }

        return rentals;
    }

    private Rental mapRow(ResultSet rs) throws SQLException {
        String rentDateStr = rs.getString("rent_date");
        String returnDateStr = rs.getString("return_date");
        return Rental.builder()
                .id(rs.getString("id"))
                .vehicle(vehicleRepo.findById(rs.getString("vehicle_id")).get())
                .user(userRepo.findById(rs.getString("user_id")).get())
                .rentDateTime(rentDateStr)
                .returnDateTime(returnDateStr)
                .build();
    }

    @Override
    public Optional<Rental> findById(String id) {
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding rental by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection()) {
            boolean exists = false;
            String checkSql = "SELECT id FROM rental WHERE id = ?";

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, rental.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }
            if (exists) {
                String updateSql = "UPDATE rental SET vehicle_id = ?, user_id = ?, rent_date = ?, return_date = ? WHERE id = ?";

                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {

                    updateStmt.setString(1, rental.getVehicleId());
                    updateStmt.setString(2, rental.getUserId());
                    updateStmt.setString(3, rental.getRentDateTime() != null ? rental.getRentDateTime() : null);
                    updateStmt.setString(4, rental.getReturnDateTime() != null ? rental.getReturnDateTime() : null);
                    updateStmt.setString(5, rental.getId());

                    updateStmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO rental (id, vehicle_id, user_id, rent_date, return_date) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, rental.getId());
                    insertStmt.setString(2, rental.getVehicleId());
                    insertStmt.setString(3, rental.getUserId());
                    insertStmt.setString(4, rental.getRentDateTime() != null ? rental.getRentDateTime() : null);
                    insertStmt.setString(5, rental.getReturnDateTime() != null ? rental.getReturnDateTime() : null);

                    insertStmt.executeUpdate();
                }
            }

            return rental;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving rental", e);
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting rental", e);
        }
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental WHERE user_id = ? AND return_date IS NULL";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding active rental for user", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental WHERE vehicle_id = ? AND return_date IS NULL";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding active rental for vehicle", e);
        }
        return Optional.empty();
    }

    public void deleteByVehicleId(String vehicleId) {
        String sql = "DELETE FROM rental WHERE vehicle_id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting rental", e);
        }
    }

    public List<Rental> findUserRentals(String userId) {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT id, vehicle_id, user_id, rent_date, return_date FROM rental WHERE user_id = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rentals.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding user rentals", e);
        }

        return rentals;
    }
}
