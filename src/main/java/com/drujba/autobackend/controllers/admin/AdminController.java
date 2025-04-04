package com.drujba.autobackend.controllers.admin;

import com.drujba.autobackend.annotations.AuditLog;
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

import java.time.Instant;
import java.util.List;
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
    @AuditLog(entityType = "User", action = "CREATE")
    public ResponseEntity<UUID> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.save(userDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PutMapping("/user/{id}/deactivate")
    @AuditLog(entityType = "User", action = "DEACTIVATE")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id, HttpServletRequest request) {
        adminService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("report/{id}")
    @AuditLog(entityType = "Report", action = "DELETE")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}