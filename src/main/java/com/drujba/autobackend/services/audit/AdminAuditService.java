package com.drujba.autobackend.services.audit;

import com.drujba.autobackend.db.entities.AdminAuditLog;
import com.drujba.autobackend.db.repositories.AdminAuditLogRepository;
import com.drujba.autobackend.models.dto.audit.AdminAuditLogDto;
import com.drujba.autobackend.services.auth.IAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAuditService {

    private final AdminAuditLogRepository auditLogRepository;
    private final IAuthService authService;
    private final ObjectMapper objectMapper;

    public void logAdminAction(String entityType, String actionType, String entityId, Object details) {
        try {
            String adminEmail = authService.getCurrentUser().getEmail();
            String detailsJson = details != null ? objectMapper.writeValueAsString(details) : null;

            AdminAuditLog auditLog = new AdminAuditLog(
                    entityType,
                    actionType,
                    entityId,
                    detailsJson,
                    adminEmail
            );

            auditLogRepository.save(auditLog);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize audit log details", e);
        } catch (Exception e) {
            log.error("Failed to log admin action", e);
        }
    }

    public Page<AdminAuditLogDto> getAuditLogs(Pageable pageable) {
        try {
            return auditLogRepository.findAll(pageable)
                    .map(this::convertToDto);
        } catch (Exception e) {
            Pageable defaultPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "timestamp")
            );
            return auditLogRepository.findAll(defaultPageable)
                    .map(this::convertToDto);
        }
    }

    public Page<AdminAuditLogDto> getAuditLogsByDateRange(Instant start, Instant end, Pageable pageable) {
        return auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(start, end, pageable)
                .map(this::convertToDto);
    }

    public Page<AdminAuditLogDto> getAuditLogsByEntityType(String entityType, Pageable pageable) {
        return auditLogRepository.findByEntityTypeOrderByTimestampDesc(entityType, pageable)
                .map(this::convertToDto);
    }

    public Page<AdminAuditLogDto> getAuditLogsByAdmin(String adminEmail, Pageable pageable) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(adminEmail, pageable)
                .map(this::convertToDto);
    }

    private AdminAuditLogDto convertToDto(AdminAuditLog auditLog) {
        AdminAuditLogDto dto = new AdminAuditLogDto();
        dto.setId(auditLog.getId());
        dto.setEntityType(auditLog.getEntityType());
        dto.setActionType(auditLog.getActionType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setDetails(auditLog.getDetails());
        dto.setPerformedBy(auditLog.getPerformedBy());
        dto.setTimestamp(auditLog.getTimestamp());
        return dto;
    }
}