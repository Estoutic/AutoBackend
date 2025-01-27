package com.drujba.autobackend.models.dto.apllication;


import com.drujba.autobackend.models.enums.application.ContactType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationCreationDto {

    private String firstName;
    private String lastName;

    private ContactType contact;
    private String contactDetails;

    private UUID branchId;
    private UUID carId;
}
