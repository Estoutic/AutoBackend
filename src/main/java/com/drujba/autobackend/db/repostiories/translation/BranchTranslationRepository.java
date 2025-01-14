package com.drujba.autobackend.db.repostiories.translation;

import com.drujba.autobackend.db.entities.translation.BranchTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchTranslationRepository extends JpaRepository<BranchTranslation, UUID> {
}