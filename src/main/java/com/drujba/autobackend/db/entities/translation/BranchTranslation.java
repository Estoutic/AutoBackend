//package com.drujba.autobackend.db.entities.translation;
//
////import com.drujba.autobackend.db.entities.Branch;
//import com.drujba.autobackend.models.dto.translation.BranchTranslationDto;
//import com.drujba.autobackend.models.enums.Locale;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@Table(name = "branch_translations")
//public class BranchTranslation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//    @ManyToOne
//    @JoinColumn(name = "branch_id", nullable = false)
//    private Branch branch;
//
//    @Enumerated(EnumType.STRING)
//    private Locale locale;
//
//    private String name;
//
//    private String address;
//
//    private String city;
//
//    private String region;
//
//    public BranchTranslation(BranchTranslationDto branchTranslationDto, Branch branch) {
//        this.id = branchTranslationDto.getId();
//        this.branch = branch;
//        this.name = branchTranslationDto.getName();
//        this.locale = branchTranslationDto.getLocale();
//        this.address = branchTranslationDto.getAddress();
//        this.city = branchTranslationDto.getCity();
//        this.region = branchTranslationDto.getRegion();
//    }
//}
