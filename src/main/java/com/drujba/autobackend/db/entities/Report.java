package com.drujba.autobackend.db.entities;

import com.drujba.autobackend.db.entities.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<Application> applications;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String filePath;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Report(String filePath) {
        this.applications = new ArrayList<>();
        this.filePath = filePath;
        this.name = "";
        this.createdAt = Instant.now();
    }

    public void addApplication(Application application) {
        this.applications.add(application);
        application.setReport(this);
    }
}