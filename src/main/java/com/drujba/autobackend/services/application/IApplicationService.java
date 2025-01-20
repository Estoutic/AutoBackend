package com.drujba.autobackend.services.application;

import com.drujba.autobackend.models.dto.apllication.ApplicationCreationDto;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IApplicationService {

    UUID saveApplication(ApplicationCreationDto applicationCreationDto);

    void deleteApplication(UUID applicationId);

    void updateApplicationStatus(UUID applicationId, ApplicationStatus status);

    ApplicationDto getApplication(UUID applicationId);

    Page<ApplicationDto> getApplications(Pageable pageable);

}
