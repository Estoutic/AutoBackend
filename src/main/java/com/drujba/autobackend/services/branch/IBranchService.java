//package com.drujba.autobackend.services.branch;
//
//import com.drujba.autobackend.models.dto.branch.BranchCreationDto;
//import com.drujba.autobackend.models.dto.branch.BranchDto;
//import com.drujba.autobackend.models.enums.Locale;
//
//import java.util.List;
//import java.util.UUID;
//
//public interface IBranchService {
//
//
//    List<BranchDto> getAllBranches(Locale locale);
//
//
//    BranchDto getBranchById(UUID id, Locale locale);
//
//    UUID saveBranch(BranchCreationDto branchCreationDTO);
//
//    void deleteBranch(UUID id);
//}