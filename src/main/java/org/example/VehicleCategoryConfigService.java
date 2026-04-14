package org.example;

import java.util.List;

public class VehicleCategoryConfigService {
    private final VehicleCategoryConfigRepository configRepo;
    public VehicleCategoryConfigService(VehicleCategoryConfigRepository configRepo) {
        this.configRepo = configRepo;
    }

    public List<VehicleCategoryConfig> findAllCategories() { return configRepo.findAll();}

    public VehicleCategoryConfig getByCategory(String category) {
        return configRepo.findByCategory(category)
                .orElseThrow(() -> new IllegalArgumentException("Nieznana kategoria pojazdu: " + category));
    }

    public boolean categoryExists(String category) {
        return configRepo.findByCategory(category).isPresent();
    }
}
