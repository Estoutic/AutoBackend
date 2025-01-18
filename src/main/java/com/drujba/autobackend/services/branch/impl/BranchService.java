package com.drujba.autobackend.services.branch.impl;


import com.drujba.autobackend.db.entities.Branch;
import com.drujba.autobackend.db.repostiories.BranchRepository;
import com.drujba.autobackend.exceptions.branch.BranchDoesNotExistException;
import com.drujba.autobackend.models.dto.branch.BranchCreationDto;
import com.drujba.autobackend.models.dto.branch.BranchDto;
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

    @Override
    public List<BranchDto> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(BranchDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public BranchDto getBranchById(UUID id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new BranchDoesNotExistException(id.toString()));
        return new BranchDto(branch);
    }

    @Override
    public UUID createBranch(BranchCreationDto branchCreationDto) {
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