package com.drujba.autobackend.aspects;

import com.drujba.autobackend.annotations.AuditLog;
import com.drujba.autobackend.services.audit.AdminAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AdminAuditService auditService;

    @AfterReturning(pointcut = "@annotation(com.drujba.autobackend.annotations.AuditLog)", returning = "result")
    public void logAdminAction(JoinPoint joinPoint, Object result) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            AuditLog auditLogAnnotation = method.getAnnotation(AuditLog.class);

            String entityType = auditLogAnnotation.entityType();
            String action = auditLogAnnotation.action();

            // Try to extract entity ID and additional details
            String entityId = extractEntityId(result, joinPoint.getArgs());
            Map<String, Object> details = extractDetails(joinPoint.getArgs());

            auditService.logAdminAction(entityType, action, entityId, details);
        } catch (Exception e) {
            log.error("Failed to process audit logging", e);
        }
    }

    private String extractEntityId(Object result, Object[] args) {
        // If result is UUID, use it as entity ID
        if (result instanceof UUID) {
            return result.toString();
        }

        // Try to find UUID in arguments
        for (Object arg : args) {
            if (arg instanceof UUID) {
                return arg.toString();
            }
        }

        return null;
    }

    private Map<String, Object> extractDetails(Object[] args) {
        Map<String, Object> details = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null &&
                    !(args[i] instanceof UUID) &&
                    !(args[i] instanceof String) &&
                    !(args[i] instanceof Number) &&
                    !(args[i] instanceof Boolean)) {
                details.put("param" + i, args[i]);
            }
        }

        return details;
    }
}