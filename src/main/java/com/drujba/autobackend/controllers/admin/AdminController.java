package com.drujba.autobackend.controllers.admin;

import com.drujba.autobackend.models.dto.report.ReportDto;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.models.dto.report.ReportFilterDto;
import com.drujba.autobackend.services.admin.IAdminService;
import com.drujba.autobackend.services.file.IReportService;
import com.drujba.autobackend.services.auth.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IAuthService authService;
    private final IAdminService adminService;
    private final IReportService reportService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/user/create")
    public ResponseEntity<UUID> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.save(userDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PutMapping("/user/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id, HttpServletRequest request) {
        adminService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("report/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
    @GetMapping("/report/all")
    public ResponseEntity<Page<ReportDto>> getAllReports(
            @RequestBody ReportFilterDto filterDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<ReportDto> reports = reportService.getFilteredReports(filterDto, pageable);
        return ResponseEntity.ok(reports);
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
    @GetMapping("/report/{id}")
    public ResponseEntity<String> getReport(@PathVariable UUID id) {
        return ResponseEntity.ok(reportService.getReportDownloadLink(id));
    }

}