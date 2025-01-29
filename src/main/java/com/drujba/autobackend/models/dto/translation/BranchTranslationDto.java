package com.drujba.autobackend.models.dto.translation;


import com.drujba.autobackend.db.entities.translation.BranchTranslation;
import com.drujba.autobackend.models.enums.Locale;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class BranchTranslationDto {

    private UUID id;
    private UUID branchId;
    private Locale locale;
    private String name;
    private String address;
    private String city;
    private String region;


    public BranchTranslationDto(BranchTranslation branchTranslation) {
        this.id = branchTranslation.getId();
        this.name = branchTranslation.getName();
        this.address = branchTranslation.getAddress();
        this.city = branchTranslation.getCity();
        this.region = branchTranslation.getRegion();
    }
}
