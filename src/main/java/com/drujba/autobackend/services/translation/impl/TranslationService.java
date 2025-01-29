package com.drujba.autobackend.services.translation.impl;

import com.drujba.autobackend.db.entities.Branch;
import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.translation.BranchTranslation;
import com.drujba.autobackend.db.entities.translation.CarTranslation;
import com.drujba.autobackend.db.repositories.BranchRepository;
import com.drujba.autobackend.db.repositories.car.CarRepository;
import com.drujba.autobackend.db.repositories.translation.BranchTranslationRepository;
import com.drujba.autobackend.db.repositories.translation.CarTranslationRepository;
import com.drujba.autobackend.exceptions.branch.BranchDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarTranslationDoesNotExistException;
import com.drujba.autobackend.models.dto.translation.BranchTranslationDto;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
import com.drujba.autobackend.services.translation.ITranslationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranslationService implements ITranslationService {

    private final CarRepository carRepository;
    private final CarTranslationRepository carTranslationRepository;
    private final BranchTranslationRepository branchTranslationRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public UUID createCarTranslation(CarTranslationDto dto) {
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new CarDoesNotExistException(dto.getCarId().toString()));

        carTranslationRepository.existsCarTranslationByLocale(dto.getLocale());
        CarTranslation carTranslation = new CarTranslation(dto, car);
        carTranslationRepository.save(carTranslation);
        return carTranslation.getId();
    }

    @Override
    @Transactional
    public void updateCarTranslation(UUID translationId, CarTranslationDto dto) {
        CarTranslation existingTranslation = carTranslationRepository.findById(translationId)
                .orElseThrow(() -> new CarTranslationDoesNotExistException(translationId.toString()));

        if (dto.getColor() != null) {
            existingTranslation.setColor(dto.getColor());
        }
        if (dto.getDescription() != null) {
            existingTranslation.setDescription(dto.getDescription());
        }
        if (dto.getMileage() != 0) {
            existingTranslation.setMileage(dto.getMileage());
        }
        if (dto.getPrice() != 0.0) {
            existingTranslation.setPrice(dto.getPrice());
        }

        carTranslationRepository.save(existingTranslation);
    }

    @Override
    @Transactional
    public void deleteCarTranslation(UUID translationId) {
        CarTranslation existingTranslation = carTranslationRepository.findById(translationId)
                .orElseThrow(() -> new CarTranslationDoesNotExistException(translationId.toString()));

        carTranslationRepository.delete(existingTranslation);
    }

    @Override
    public List<CarTranslationDto> getCarTranslations(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(() ->
                new CarDoesNotExistException(carId.toString()));
        List<CarTranslation> carTranslations = carTranslationRepository.findAllByCar(car);
        return carTranslations.stream().map(CarTranslationDto::new).toList();
    }

    @Override
    public UUID createBranchTranslation(BranchTranslationDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new BranchDoesNotExistException(dto.getBranchId().toString()));

        branchTranslationRepository.existsBranchTranslationByLocale(dto.getLocale());
        BranchTranslation branchTranslation = new BranchTranslation(dto, branch);
        branchTranslationRepository.save(branchTranslation);
        return branchTranslation.getId();
    }

    @Override
    public void updateBranchTranslation(UUID translationId, BranchTranslationDto dto) {
        BranchTranslation branchTranslation = branchTranslationRepository.findById(translationId).orElseThrow(
                () -> new BranchDoesNotExistException(dto.getBranchId().toString()));
        if (dto.getName() != null) {
            branchTranslation.setName(dto.getName());
        }
        if (dto.getCity() != null) {
            branchTranslation.setCity(dto.getCity());
        }
        if (dto.getAddress() != null) {
            branchTranslation.setAddress(dto.getAddress());
        }
        if (dto.getRegion() != null) {
            branchTranslation.setRegion(dto.getRegion());
        }
        branchTranslationRepository.save(branchTranslation);
    }

    @Override
    public void deleteBranchTranslation(UUID branchId) {

        BranchTranslation branchTranslation = branchTranslationRepository.findById(branchId).orElseThrow(
                () -> new BranchDoesNotExistException(branchId.toString()));
        branchTranslationRepository.delete(branchTranslation);
    }

    @Override
    public List<BranchTranslationDto> getBranchTranslations(UUID branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() ->
                new BranchDoesNotExistException(branchId.toString()));
        List<BranchTranslation> branchTranslations = branchTranslationRepository.findAllByBranch(branch);
        return branchTranslations.stream().map(BranchTranslationDto::new).toList();
    }
}
