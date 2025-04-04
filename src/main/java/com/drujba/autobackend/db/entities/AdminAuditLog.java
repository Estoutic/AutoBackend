package com.drujba.autobackend.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "admin_audit_logs")
public class AdminAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String entityType;  // Car, Application, User, etc.

    @Column(nullable = false)
    private String actionType;  // CREATE, UPDATE, DELETE

    @Column
    private String entityId;    // ID of the affected entity

    @Column(length = 1000)
    private String details;     // JSON or details about the changes

    @Column(nullable = false)
    private String performedBy; // Email of the admin who performed the action

    @CreationTimestamp
    private Instant timestamp;

    public AdminAuditLog(String entityType, String actionType, String entityId, String details, String performedBy) {
        this.entityType = entityType;
        this.actionType = actionType;
        this.entityId = entityId;
        this.details = details;
        this.performedBy = performedBy;
    }
}