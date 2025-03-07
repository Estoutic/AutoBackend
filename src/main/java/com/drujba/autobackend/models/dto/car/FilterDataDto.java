package com.drujba.autobackend.models.dto.car;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class FilterDataDto {
    private List<String> brands;
    private Map<String, List<String>> models;
    private Map<String, List<String>> generations;
}
