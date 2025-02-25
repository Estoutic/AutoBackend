//package com.drujba.autobackend.models.dto.branch;
//
//import com.drujba.autobackend.db.entities.Branch;
//import com.drujba.autobackend.db.entities.translation.BranchTranslation;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.UUID;
//
//@Data
//@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class BranchDto {
//    private UUID id;
//    private String name;
//    private String address;
//    private String city;
//    private String region;
//    private String phone;
//    private String email;
//    private String workingHours;
//    private Double latitude;
//    private Double longitude;
//
//    public BranchDto(Branch branch) {
//        this.id = branch.getId();
//        this.name = branch.getName();
//        this.address = branch.getAddress();
//        this.city = branch.getCity();
//        this.region = branch.getRegion();
//        this.phone = branch.getPhone();
//        this.email = branch.getEmail();
//        this.workingHours = branch.getWorkingHours();
//        this.latitude = branch.getLatitude();
//        this.longitude = branch.getLongitude();
//    }
//
//    public BranchDto(Branch branch, BranchTranslation translation) {
//        this.id = branch.getId();
//        this.name = translation.getName();
//        this.address = translation.getAddress();
//        this.city = translation.getCity();
//        this.region = translation.getRegion();
//        this.phone = branch.getPhone();
//        this.email = branch.getEmail();
//        this.workingHours = branch.getWorkingHours();
//        this.latitude = branch.getLatitude();
//        this.longitude = branch.getLongitude();
//    }
//}