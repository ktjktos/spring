package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleCategoryConfigJsonRepsitory implements VehicleCategoryConfigRepository{
    private List<VehicleCategoryConfig> configs;

    public VehicleCategoryConfigJsonRepsitory() {
        Path path = Path.of("categories.json");
        configs = new ArrayList<>();
        try {
            if (Files.notExists(path)) {
                Files.writeString(path,"[]");
            }
            String json = Files.readString(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            configs = gson.fromJson(json, new TypeToken<List<VehicleCategoryConfig>>() {}.getType());
        } catch (IOException e) {
            System.out.println("cos poszlo nie tak w ladowaniu configu vehicle category");
        }
    }

    @Override
    public List<VehicleCategoryConfig> findAll() {
        List<VehicleCategoryConfig> copy = new ArrayList<>();
        for (VehicleCategoryConfig config: configs) {
            copy.add(config.copy());
        }
        return copy;
    }

    @Override
    public Optional<VehicleCategoryConfig> findByCategory(String category) {
        return configs.stream()
                .filter(c -> c.getCategory() != null)
                .filter(c -> c.getCategory().equalsIgnoreCase(category))
                .findFirst()
                .map(VehicleCategoryConfig::copy);
    }
}
