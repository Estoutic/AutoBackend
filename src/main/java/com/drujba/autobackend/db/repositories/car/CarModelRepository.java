package com.drujba.autobackend.db.repositories.car;

import com.drujba.autobackend.db.entities.car.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, UUID> {
    Optional<CarModel> findByBrandAndModelAndGeneration(String brand, String model, String generation);

    Optional<CarModel> findByGeneration(String generation);

    Boolean existsByBrandAndModelAndGeneration(String brand, String model, String generation);


    @Query("SELECT DISTINCT cm.brand FROM CarModel cm")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT cm.model FROM CarModel cm WHERE cm.brand = :brand")
    List<String> findDistinctModelsByBrand(@Param("brand") String brand);

    @Query("SELECT DISTINCT cm.generation FROM CarModel cm WHERE cm.model = :model")
    List<String> findDistinctGenerationsByModel(@Param("model") String model);

    @Query("SELECT cm.brand, cm.model, cm.generation FROM CarModel cm ORDER BY cm.brand, cm.model, cm.generation")
    List<Object[]> findAllBrandsModelsGeneration();
}