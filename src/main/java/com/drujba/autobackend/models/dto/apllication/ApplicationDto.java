package com.drujba.autobackend.models.dto.apllication;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.db.entities.Branch;
import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.models.dto.branch.BranchDto;
import com.drujba.autobackend.models.dto.car.CarDto;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import com.drujba.autobackend.models.enums.application.ContactType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDto {

    private UUID id;
    private CarDto car;

    private BranchDto branch;

    private String firstName;
    private String lastName;

    private ContactType contact;

    private ApplicationStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    public ApplicationDto(Application application) {
        this.id = application.getId();
        this.car = new CarDto(application.getCar());
        this.branch = new BranchDto(application.getBranch());
        this.firstName = application.getFirstName();
        this.lastName = application.getLastName();
        this.contact = application.getContact();
        this.status = application.getStatus();
        this.createdAt = application.getCreatedAt();
        this.updatedAt = application.getUpdatedAt();
    }
}
