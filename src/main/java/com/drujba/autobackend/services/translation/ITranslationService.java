package com.drujba.autobackend.services.translation;

import com.drujba.autobackend.models.dto.translation.BranchTranslationDto;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;

import java.util.List;
import java.util.UUID;

public interface ITranslationService {

    UUID createCarTranslation(CarTranslationDto dto);

    void updateCarTranslation(UUID translationId, CarTranslationDto dto);

    void deleteCarTranslation(UUID translationId);

    List<CarTranslationDto> getCarTranslations(UUID carId);


    UUID createBranchTranslation(BranchTranslationDto dto);

    void updateBranchTranslation(UUID translationId, BranchTranslationDto dto);

    void deleteBranchTranslation(UUID branchId);

    List<BranchTranslationDto> getBranchTranslations(UUID carId);
}
