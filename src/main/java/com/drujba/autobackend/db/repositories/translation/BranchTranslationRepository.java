package com.drujba.autobackend.db.repositories.translation;

import com.drujba.autobackend.db.entities.Branch;
import com.drujba.autobackend.db.entities.translation.BranchTranslation;
import com.drujba.autobackend.models.enums.Locale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BranchTranslationRepository extends JpaRepository<BranchTranslation, UUID> {

    Boolean existsBranchTranslationByLocale(Locale locale);

    List<BranchTranslation> findAllByBranch(Branch branch);
}