package com.drujba.autobackend.db.entities;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.models.dto.apllication.ApplicationCreationDto;
import com.drujba.autobackend.models.enums.application.ContactType;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

//    @ManyToOne
//    @JoinColumn(name = "branch_id", nullable = false)
//    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private ContactType contact;

    private String contactDetails;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

//    public Application(ApplicationCreationDto applicationCreationDto, Car car, Branch branch) {
//        this.firstName = applicationCreationDto.getFirstName();
//        this.lastName = applicationCreationDto.getLastName();
//        this.contact = applicationCreationDto.getContact();
//        this.contactDetails = applicationCreationDto.getContactDetails();
//        this.car = car;
////        this.branch = branch;
//        this.status = ApplicationStatus.ACCEPTED;
//        this.report = null;
//    }
    public Application(ApplicationCreationDto applicationCreationDto, Car car) {
        this.firstName = applicationCreationDto.getFirstName();
        this.lastName = applicationCreationDto.getLastName();
        this.contact = applicationCreationDto.getContact();
        this.contactDetails = applicationCreationDto.getContactDetails();
        this.car = car;
        this.status = ApplicationStatus.ACCEPTED;
        this.report = null;
    }
}