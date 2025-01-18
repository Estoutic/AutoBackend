package com.drujba.autobackend.models.dto.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchCreationDto {
    private String name;
    private String address;
    private String city;
    private String region;
    private String phone;
    private String email;
    private String workingHours;
    private Double latitude;
    private Double longitude;
}