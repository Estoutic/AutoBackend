package com.drujba.autobackend.db.entities;

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
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String address;

    private String city;

    private String region;

    private String phone;

    private String email;

    @Column(name = "working_hours")
    private String workingHours;

    private Double latitude;

    private Double longitude;

    @CreationTimestamp
    private Instant createdAt;
}
