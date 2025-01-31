package com.drujba.autobackend.services.branch.impl;


import com.drujba.autobackend.db.entities.Branch;
import com.drujba.autobackend.db.entities.translation.BranchTranslation;
import com.drujba.autobackend.db.repositories.BranchRepository;
import com.drujba.autobackend.db.repositories.translation.BranchTranslationRepository;
import com.drujba.autobackend.exceptions.branch.BranchDoesNotExistException;
import com.drujba.autobackend.models.dto.branch.BranchCreationDto;
import com.drujba.autobackend.models.dto.branch.BranchDto;
import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.services.branch.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService implements IBranchService {

    private final BranchRepository branchRepository;
    private final BranchTranslationRepository branchTranslationRepository;

    @Override
    public List<BranchDto> getAllBranches(Locale locale) {
        return branchRepository.findAll().stream()
                .map(branch -> {
                    BranchTranslation translation = branchTranslationRepository
                            .findFirstByBranchAndLocale(branch, locale)
                            .orElseGet(() -> branchTranslationRepository
                                    .findFirstByBranchAndLocale(branch, Locale.EU)
                                    .orElse(null));

                    return translation != null ? new BranchDto(branch, translation) : new BranchDto(branch);
                })
                .collect(Collectors.toList());
    }

    @Override
    public BranchDto getBranchById(UUID id, Locale locale) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new BranchDoesNotExistException(id.toString()));

        BranchTranslation translation = branchTranslationRepository
                .findFirstByBranchAndLocale(branch, locale)
                .orElseGet(() -> branchTranslationRepository
                        .findFirstByBranchAndLocale(branch, Locale.EU)
                        .orElse(null));

        return translation != null ? new BranchDto(branch, translation) : new BranchDto(branch);
    }

    @Override
    public UUID saveBranch(BranchCreationDto branchCreationDto) {
        Branch branch = new Branch(branchCreationDto);
        return branchRepository.save(branch).getId();
    }

    @Override
    public void deleteBranch(UUID id) {
        if (!branchRepository.existsById(id)) {
            throw new BranchDoesNotExistException(id.toString());
        }
        branchRepository.deleteById(id);
    }
}