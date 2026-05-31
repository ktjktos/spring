package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.model.VehicleCategoryConfig;
import org.example.repository.IVehicleCategoryConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleCategoryConfigService {
    private final IVehicleCategoryConfigRepository configRepo;

    @Transactional(readOnly = true)
    public List<VehicleCategoryConfig> findAllCategories() { return configRepo.findAll();}

    @Transactional(readOnly = true)
    public VehicleCategoryConfig getByCategory(String category) {
        return configRepo.findByCategory(category)
                .orElseThrow(() -> new IllegalArgumentException("Nieznana kategoria pojazdu: " + category));
    }

    @Transactional(readOnly = true)
    public boolean categoryExists(String category) {
        return configRepo.findByCategory(category).isPresent();
    }
}
