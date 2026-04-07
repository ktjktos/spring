package org.example;
import java.util.List;

public interface IVehicleRepository {
    Boolean returnVehicle(String id);

    Boolean rentVehicle(String id);


    List<Vehicle> getVehicles();
    void save();
    void load();

    void add(String data);
    void remove(String id);
    Vehicle getVehicle(String id);
}
