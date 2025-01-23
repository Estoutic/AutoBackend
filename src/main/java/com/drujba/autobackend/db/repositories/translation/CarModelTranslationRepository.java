package com.drujba.autobackend.db.repositories.translation;

import com.drujba.autobackend.db.entities.translation.CarModelTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarModelTranslationRepository extends JpaRepository<CarModelTranslation, UUID> {
}