package com.drujba.autobackend.db.repositories.translation;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.translation.CarTranslation;
import com.drujba.autobackend.models.enums.Locale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarTranslationRepository extends JpaRepository<CarTranslation, UUID> {

   Boolean existsCarTranslationByLocale(Locale locale);

   List<CarTranslation> findAllByCar(Car car);

   Optional<CarTranslation> findByCarAndLocale(Car car, Locale locale);
}