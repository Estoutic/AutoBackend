//package com.drujba.autobackend.db.entities;
//
//import com.drujba.autobackend.models.dto.branch.BranchCreationDto;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.Instant;
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@Table(name = "branches")
//public class Branch {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    private String name;
//
//    private String address;
//
//    private String city;
//
//    private String region;
//
//    private String phone;
//
//    private String email;
//
//    @Column(name = "working_hours")
//    private String workingHours;
//
//    private Double latitude;
//
//    private Double longitude;
//
//    @CreationTimestamp
//    private Instant createdAt;
//
//    public Branch(BranchCreationDto dto) {
//        this.name = dto.getName();
//        this.address = dto.getAddress();
//        this.city = dto.getCity();
//        this.region = dto.getRegion();
//        this.phone = dto.getPhone();
//        this.email = dto.getEmail();
//        this.workingHours = dto.getWorkingHours();
//        this.latitude = dto.getLatitude();
//        this.longitude = dto.getLongitude();
//    }
//
//}
