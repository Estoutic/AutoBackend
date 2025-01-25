package com.drujba.autobackend.services.file.impl;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.db.entities.Report;
import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.db.repositories.ApplicationRepository;
import com.drujba.autobackend.db.repositories.ReportRepository;
import com.drujba.autobackend.exceptions.application.ReportDoesNotExist;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.models.dto.report.ReportDto;
import com.drujba.autobackend.models.dto.report.ReportFilterDto;
import com.drujba.autobackend.models.enums.BucketType;
import com.drujba.autobackend.services.file.IReportService;
import com.drujba.autobackend.services.auth.IAuthService;
import com.drujba.autobackend.services.file.IMinioService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final IAuthService authService;
    private final ReportRepository reportRepository;
    private final ApplicationRepository applicationRepository;
    private final IMinioService minioService;

    @Override
    public void generateReport(UUID applicationId) {
        User user = authService.getCurrentUser();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        byte[] fileContent = createExcelReport(application);

        String fileName = String.format("reports/%s-%s.xlsx", application.getId(), Instant.now().toEpochMilli());

        String filePath = minioService.uploadFile(BucketType.REPORT, fileName, new ByteArrayInputStream(fileContent), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        Report report = new Report(application, fileName, filePath, user);
        reportRepository.save(report);

        new ReportDto(report);
    }

    @Override
    public Page<ReportDto> getFilteredReports(ReportFilterDto filterDto, Pageable pageable) {
        Page<Report> reports = reportRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getCreatedAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filterDto.getCreatedAfter()));
            }
            if (filterDto.getCreatedBefore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filterDto.getCreatedBefore()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return reports.map(report -> new ReportDto(
                report.getId(),
                new UserDto(report.getUser()),
                new ApplicationDto(report.getApplication()),
                report.getName(),
                report.getFilePath(),
                report.getCreatedAt()
        ));
    }

    @Override
    public String getReportDownloadLink(UUID id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new ReportDoesNotExist(id.toString()));
        return report.getFilePath();
    }

    @Override
    public void deleteReport(UUID id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new ReportDoesNotExist(id.toString()));
        String fileName = report.getFilePath().substring(report.getFilePath().lastIndexOf('/') + 1);

        minioService.deleteFile(BucketType.REPORT, fileName);
        reportRepository.deleteById(id);
    }

//  TODO разобратсья почему не удаляются

    private byte[] createExcelReport(Application application) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sales Report");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Поле");
            headerRow.createCell(1).setCellValue("Значение");

            int rowIdx = 1;

            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("ID Заявки");
            row.createCell(1).setCellValue(application.getId().toString());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Имя пользователя");
            row.createCell(1).setCellValue(application.getFirstName() + " " + application.getLastName());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Марка");
            row.createCell(1).setCellValue(application.getCar().getCarModel().getBrand());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Модель");
            row.createCell(1).setCellValue(application.getCar().getCarModel().getModel());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Год выпуска");
            row.createCell(1).setCellValue(application.getCar().getYear());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Тип кузова");
            row.createCell(1).setCellValue(application.getCar().getBodyType().name());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Стоимость");
            row.createCell(1).setCellValue(application.getCar().getPrice());

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании Excel отчета", e);
        }
    }
}

// TODO сделать кастомную ошибку тут
