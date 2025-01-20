package com.drujba.autobackend.services.application.impl;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.db.entities.Branch;
import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.repostiories.ApplicationRepository;
import com.drujba.autobackend.db.repostiories.BranchRepository;
import com.drujba.autobackend.db.repostiories.car.CarRepository;
import com.drujba.autobackend.exceptions.application.ApplicationDoesNotExistException;
import com.drujba.autobackend.exceptions.branch.BranchDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.models.dto.apllication.ApplicationCreationDto;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import com.drujba.autobackend.services.application.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService implements IApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CarRepository carRepository;
    private final BranchRepository branchRepository;

    @Override
    public UUID saveApplication(ApplicationCreationDto applicationCreationDto) {
        Car car = carRepository.findById(applicationCreationDto.getCarId()).orElseThrow(()
                -> new CarDoesNotExistException(applicationCreationDto.getCarId().toString()));
        Branch branch = branchRepository.findById(applicationCreationDto.getBranchId()).orElseThrow(() ->
                new BranchDoesNotExistException(applicationCreationDto.getBranchId().toString()));
        Application application = new Application(applicationCreationDto, car, branch);
        return applicationRepository.save(application).getId();
    }

    @Override
    public void deleteApplication(UUID applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ApplicationDoesNotExistException(applicationId.toString());
        }
        applicationRepository.deleteById(applicationId);
    }

    @Override
    public void updateApplicationStatus(UUID applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationDoesNotExistException(applicationId.toString()));
        application.setStatus(status);
        applicationRepository.save(application);
    }

    @Override
    public ApplicationDto getApplication(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() ->
                new ApplicationDoesNotExistException(applicationId.toString()));
        return new ApplicationDto(application);
    }

    @Override
    public Page<ApplicationDto> getApplications(Pageable pageable) {
        return applicationRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(ApplicationDto::new);
    }

}
