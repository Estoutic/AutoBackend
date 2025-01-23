package com.drujba.autobackend.models.dto.report;

import com.drujba.autobackend.db.entities.Report;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class ReportDto {

    private UUID id;

    private UserDto user;

    private ApplicationDto application;

    private String name;

    private String filePath;

    private Instant createdAt;

    public ReportDto(Report report) {
        this.filePath = report.getFilePath();
        this.name = report.getName();
        this.application = new ApplicationDto(report.getApplication());
        this.user = new UserDto(report.getUser());
        this.id = report.getId();
        this.createdAt = report.getCreatedAt();
    }
}
