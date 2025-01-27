package com.drujba.autobackend.services.file.impl;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.db.entities.Report;
import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.db.repositories.ApplicationRepository;
import com.drujba.autobackend.db.repositories.ReportRepository;
import com.drujba.autobackend.exceptions.application.ApplicationDoesNotExistException;
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
import jakarta.transaction.Transactional;
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
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
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
    @Transactional
    public void generateReport(UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationDoesNotExistException(applicationId.toString()));
//        if (application.getReport() != null){
//            return;
//        }
        Instant startOfMonth = Instant.now().atZone(ZoneId.systemDefault())
                .with(TemporalAdjusters.firstDayOfMonth())
                .toInstant();
        Instant endOfMonth = Instant.now().atZone(ZoneId.systemDefault())
                .with(TemporalAdjusters.lastDayOfMonth())
                .toInstant();

        Report report = reportRepository.findFirstByCreatedAtBetween(startOfMonth, endOfMonth)
                .orElseGet(() -> {
                    Report newReport = new Report("");
                    reportRepository.save(newReport);
                    String fileName = String.format("%s.xlsx", newReport.getId().toString());
                    newReport.setName(fileName);
                    reportRepository.save(newReport);
                    return newReport;
                });

        report.addApplication(application);
        reportRepository.save(report);

        byte[] updatedFileContent = updateExcelFile(report);

        String filePath = minioService.uploadFile(BucketType.REPORT, report.getName(),
                new ByteArrayInputStream(updatedFileContent), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        report.setFilePath(filePath);
        reportRepository.save(report);
    }
    private byte[] updateExcelFile(Report report) {
        Workbook workbook;

        try {
            String filePath = report.getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                ByteArrayInputStream existingFile = minioService.downloadFile(BucketType.REPORT, report.getName());
                workbook = new XSSFWorkbook(existingFile);
            } else {
                workbook = new XSSFWorkbook();
            }

            Sheet sheet = workbook.getSheet("Applications Report");
            if (sheet == null) {
                sheet = workbook.createSheet("Applications Report");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Имя");
                headerRow.createCell(1).setCellValue("Фамилия");
                headerRow.createCell(2).setCellValue("Марка");
                headerRow.createCell(3).setCellValue("VIN номер");
                headerRow.createCell(4).setCellValue("Тип контакта");
                headerRow.createCell(5).setCellValue("Детали контакта");
            }

            int rowIdx = sheet.getLastRowNum() + 1;

            for (Application application : report.getApplications()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(application.getFirstName());
                row.createCell(1).setCellValue(application.getLastName());
                row.createCell(2).setCellValue(application.getCar().getCarModel().getBrand());
                row.createCell(3).setCellValue(application.getCar().getVin());
                row.createCell(4).setCellValue(application.getContact().name());
                row.createCell(5).setCellValue(application.getContactDetails());
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении Excel файла", e);
        }
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
                report.getApplications(),
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
}

// TODO сделать кастомную ошибку тут
