package com.drujba.autobackend.db.repostiories.auto;

import com.drujba.autobackend.db.entities.auto.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, UUID> {
    Optional<CarModel> findByBrandAndModelAndGeneration(String brand, String model, String generation);

    Boolean existsByBrandAndModelAndGeneration(String brand, String model, String generation);
}