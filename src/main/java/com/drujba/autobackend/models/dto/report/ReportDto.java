package com.drujba.autobackend.models.dto.report;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.db.entities.Report;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class ReportDto {

    private UUID id;

    @JsonManagedReference
    private List<ApplicationDto> applications;

    private String name;

    private String filePath;

    private Instant createdAt;

    public ReportDto(Report report) {
        this.filePath = report.getFilePath();
        this.name = report.getName();
        this.applications = report.getApplications().stream().map(application -> new ApplicationDto(application, this)).collect(Collectors.toList());
        this.id = report.getId();
        this.createdAt = report.getCreatedAt();
    }

    public ReportDto(Report report, List<Application> applications) {
        this.filePath = report.getFilePath();
        this.name = report.getName();
        this.applications = applications.stream().map(application -> new ApplicationDto(application, this)).collect(Collectors.toList());
        this.id = report.getId();
        this.createdAt = report.getCreatedAt();
    }

}
