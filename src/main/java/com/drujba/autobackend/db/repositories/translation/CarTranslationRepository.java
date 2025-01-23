package com.drujba.autobackend.db.repositories.translation;

import com.drujba.autobackend.db.entities.translation.CarTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarTranslationRepository extends JpaRepository<CarTranslation, UUID> {
}