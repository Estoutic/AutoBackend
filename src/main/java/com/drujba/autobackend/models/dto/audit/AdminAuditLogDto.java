package com.drujba.autobackend.models.dto.audit;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AdminAuditLogDto {
    private UUID id;
    private String entityType;
    private String actionType;
    private String entityId;
    private String details;
    private String performedBy;
    private Instant timestamp;
}