package com.drujba.autobackend.services.branch;

import com.drujba.autobackend.models.dto.branch.BranchCreationDto;
import com.drujba.autobackend.models.dto.branch.BranchDto;

import java.util.List;
import java.util.UUID;

public interface IBranchService {

    List<BranchDto> getAllBranches();

    BranchDto getBranchById(UUID id);

    UUID createBranch(BranchCreationDto branchCreationDTO);

    void deleteBranch(UUID id);
}