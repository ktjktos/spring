package org.example.repository.impl.jdbc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.example.model.Vehicle;
import org.example.repository.IVehicleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class VehicleJdbcRepository implements IVehicleRepository {

    private final Gson gson = new Gson();
    private final Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
    private final DataSource dataSource;

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT id, type_of_vehicle, brand, model, production_year, plate, price, attributes FROM vehicle";

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading vehicles", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

        return vehicles;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        String sql = "SELECT id, type_of_vehicle, brand, model, production_year, plate, price, attributes FROM vehicle WHERE id = ?";

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding vehicle by id", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return Optional.empty();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        String attributesJson = gson.toJson(vehicle.getAttributes());

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            boolean exists = false;
            String checkSql = "SELECT id FROM vehicle WHERE id = ?";

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, vehicle.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }
            if (exists) {
                String updateSql = "UPDATE vehicle SET type_of_vehicle = ?, brand = ?, model = ?, " +
                        "production_year = ?, plate = ?, price = ?, attributes = ?::jsonb WHERE id = ?";

                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, vehicle.getTypeOfVehicle());
                    updateStmt.setString(2, vehicle.getBrand());
                    updateStmt.setString(3, vehicle.getModel());
                    updateStmt.setInt(4, vehicle.getYear());
                    updateStmt.setString(5, vehicle.getPlate());
                    updateStmt.setInt(6, vehicle.getPrice());
                    updateStmt.setString(7, attributesJson);
                    updateStmt.setString(8, vehicle.getId());

                    updateStmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO vehicle (id, type_of_vehicle, brand, model, " +
                        "production_year, plate, price, attributes) VALUES (?, ?, ?, ?, ?, ?, ?, ?::jsonb)";

                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, vehicle.getId());
                    insertStmt.setString(2, vehicle.getTypeOfVehicle());
                    insertStmt.setString(3, vehicle.getBrand());
                    insertStmt.setString(4, vehicle.getModel());
                    insertStmt.setInt(5, vehicle.getYear());
                    insertStmt.setString(6, vehicle.getPlate());
                    insertStmt.setInt(7, vehicle.getPrice());
                    insertStmt.setString(8, attributesJson);

                    insertStmt.executeUpdate();
                }
            }

            return vehicle;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving vehicle", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM vehicle WHERE id = ?";
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
    private Vehicle mapRow(ResultSet rs) throws SQLException {
        String attrJson = rs.getString("attributes");
        Map<String, Object> attributes = gson.fromJson(attrJson, mapType);

        return Vehicle.builder()
                .id(rs.getString("id"))
                .typeOfVehicle(rs.getString("type_of_vehicle"))
                .brand(rs.getString("brand"))
                .model(rs.getString("model"))
                .year(rs.getInt("production_year"))
                .plate(rs.getString("plate"))
                .price(rs.getInt("price"))
                .attributes(attributes != null ? attributes : new HashMap<>())
                .build();
    }
}