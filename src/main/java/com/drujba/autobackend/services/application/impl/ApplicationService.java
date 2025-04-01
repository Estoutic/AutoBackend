package com.drujba.autobackend.services.application.impl;

import com.drujba.autobackend.db.entities.Application;
//import com.drujba.autobackend.db.entities.Branch;
import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.repositories.ApplicationRepository;
//import com.drujba.autobackend.db.repositories.BranchRepository;
import com.drujba.autobackend.db.repositories.car.CarRepository;
import com.drujba.autobackend.exceptions.application.ApplicationDoesNotExistException;
import com.drujba.autobackend.exceptions.application.StatusAlreadySetException;
import com.drujba.autobackend.exceptions.branch.BranchDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.models.dto.apllication.ApplicationCreationDto;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import com.drujba.autobackend.models.enums.application.ContactType;
import com.drujba.autobackend.services.file.IReportService;
import com.drujba.autobackend.services.application.IApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService implements IApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CarRepository carRepository;
    //    private final BranchRepository branchRepository;
    private final IReportService reportService;

    @Override
    public UUID saveApplication(ApplicationCreationDto applicationCreationDto) {
        validateContactDetails(applicationCreationDto.getContact(), applicationCreationDto.getContactDetails());
        Car car = carRepository.findById(applicationCreationDto.getCarId()).orElseThrow(()
                -> new CarDoesNotExistException(applicationCreationDto.getCarId().toString()));
//        Branch branch = branchRepository.findById(applicationCreationDto.getBranchId()).orElseThrow(() ->
//                new BranchDoesNotExistException(applicationCreationDto.getBranchId().toString()));
        Application application = new Application(applicationCreationDto, car);
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
    @Transactional
    public void updateApplicationStatus(UUID applicationId, ApplicationStatus status) {

        log.info(status.toString());
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationDoesNotExistException(applicationId.toString()));
        if (application.getStatus() == status) {
            throw new StatusAlreadySetException(status.toString(), applicationId.toString());
        }
        if (status == ApplicationStatus.COMPLETED) {
            reportService.generateReport(applicationId);
            log.info("Application {} has been completed", applicationId);
        }
        application.setStatus(status);
        applicationRepository.save(application);
    }

    @Override
    public ApplicationDto getApplication(UUID applicationId, Locale locale) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() ->
                new ApplicationDoesNotExistException(applicationId.toString()));
        return new ApplicationDto(application, locale);
    }


    /**
     * Get all applications
     */
    public Page<ApplicationDto> getApplications(Pageable pageable, Locale locale) {
        return applicationRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(application -> new ApplicationDto(application, locale));
    }

    /**
     * Get applications by status
     */
    public Page<ApplicationDto> getApplicationsByStatus(Pageable pageable, ApplicationStatus status, Locale locale) {
        return applicationRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable)
                .map(application -> new ApplicationDto(application, locale));
    }

    /**
     * Get applications with filtering by status and creation date
     */
    public Page<ApplicationDto> getApplicationsByFilters(
            Pageable pageable,
            ApplicationStatus status,
            Instant createdAfter,
            Instant createdBefore,
            Locale locale) {

        Page<Application> applications;

        // Handle different filter combinations
        if (status != null) {
            if (createdAfter != null && createdBefore != null) {
                applications = applicationRepository.findAllByStatusAndCreatedAtBetweenOrderByCreatedAtDesc(
                        status, createdAfter, createdBefore, pageable);
            } else if (createdAfter != null) {
                applications = applicationRepository.findAllByStatusAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
                        status, createdAfter, pageable);
            } else if (createdBefore != null) {
                applications = applicationRepository.findAllByStatusAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
                        status, createdBefore, pageable);
            } else {
                applications = applicationRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable);
            }
        } else {
            if (createdAfter != null && createdBefore != null) {
                applications = applicationRepository.findAllByCreatedAtBetweenOrderByCreatedAtDesc(
                        createdAfter, createdBefore, pageable);
            } else if (createdAfter != null) {
                applications = applicationRepository.findAllByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
                        createdAfter, pageable);
            } else if (createdBefore != null) {
                applications = applicationRepository.findAllByCreatedAtLessThanEqualOrderByCreatedAtDesc(
                        createdBefore, pageable);
            } else {
                applications = applicationRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        return applications.map(application -> new ApplicationDto(application, locale));
    }

    public void validateContactDetails(ContactType contactType, String contactDetails) {
        if (contactType == ContactType.CALL && !contactDetails.matches("\\+\\d{1,15}")) {
            throw new IllegalArgumentException("Invalid phone number format for CALL contact type.");
        } else if ((contactType == ContactType.EMAIL && !contactDetails.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$"))) {
            throw new IllegalArgumentException("Invalid email format for EMAIL contact type.");
        } else if ((contactType == ContactType.WHATSAPP || contactType == ContactType.TELEGRAM) && contactDetails.isBlank()) {
            throw new IllegalArgumentException("Username is required for WHATSAPP or TELEGRAM contact type.");
        }
    }
}
