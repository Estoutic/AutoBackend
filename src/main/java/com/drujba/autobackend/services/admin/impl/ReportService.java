package com.drujba.autobackend.services.admin.impl;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.db.entities.Report;
import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.db.repositories.ApplicationRepository;
import com.drujba.autobackend.db.repositories.ReportRepository;
import com.drujba.autobackend.models.dto.ReportDto;
import com.drujba.autobackend.services.admin.IReportService;
import com.drujba.autobackend.services.auth.IAuthService;
import com.drujba.autobackend.services.file.IMinioService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
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

        String filePath = minioService.uploadReport(fileName, new ByteArrayInputStream(fileContent), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        Report report = new Report(application, fileName, filePath, user);
        reportRepository.save(report);

        new ReportDto(report);
    }

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
