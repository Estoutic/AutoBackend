package com.drujba.autobackend.services.admin;

import com.drujba.autobackend.models.dto.report.ReportDto;
import com.drujba.autobackend.models.dto.report.ReportFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IReportService {

    void generateReport(UUID id);

    Page<ReportDto> getFilteredReports(ReportFilterDto filterDto, Pageable pageable);

    String getReportDownloadLink(UUID id);

}
