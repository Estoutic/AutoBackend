package com.drujba.autobackend.db.entities.translation;

import com.drujba.autobackend.db.entities.Branch;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "branch_translations")
public class BranchTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    private String locale;

    private String name;

    private String address;

    private String city;

    private String region;
}
