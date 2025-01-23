package com.drujba.autobackend.models.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportFilterDto {

    private Instant createdAfter;
    private Instant createdBefore;
}
