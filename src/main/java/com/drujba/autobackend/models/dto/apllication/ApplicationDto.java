package com.drujba.autobackend.models.dto.apllication;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.models.dto.car.CarResponseDto;
import com.drujba.autobackend.models.dto.report.ReportDto;
import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import com.drujba.autobackend.models.enums.application.ContactType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDto {

    private UUID id;
    private CarResponseDto car;

//    private BranchDto branch;

    @JsonBackReference
    private ReportDto report;

    private String firstName;
    private String lastName;

    private ContactType contact;

    private String contactDetails;

    private ApplicationStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    public ApplicationDto(Application application, Locale locale) {
        this.id = application.getId();
        this.car = new CarResponseDto(application.getCar(), locale);
//        this.branch = new BranchDto(application.getBranch());
        this.firstName = application.getFirstName();
        this.lastName = application.getLastName();
        this.contact = application.getContact();
        this.contactDetails = application.getContactDetails();
        this.status = application.getStatus();
        this.createdAt = application.getCreatedAt();
        this.updatedAt = application.getUpdatedAt();
        if (application.getReport() != null) {
            this.report = new ReportDto(application.getReport(), application.getReport().getApplications());
        }
    }

    public ApplicationDto(Application application, ReportDto report) {
        this.id = application.getId();
        this.car = new CarResponseDto(application.getCar());
//        this.branch = new BranchDto(application.getBranch());
        this.firstName = application.getFirstName();
        this.lastName = application.getLastName();
        this.contact = application.getContact();
        this.status = application.getStatus();
        this.createdAt = application.getCreatedAt();
        this.updatedAt = application.getUpdatedAt();
        this.report = report;
//        this.report = new ReportDto(application.getReport(), application.getReport().getApplications());
    }
}
