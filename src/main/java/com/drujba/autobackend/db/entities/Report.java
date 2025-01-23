package com.drujba.autobackend.db.entities;

import com.drujba.autobackend.db.entities.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String filePath;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    public Report(Application application, String name, String filePath, User user) {
        this.application = application;
        this.name = name;
        this.filePath = filePath;
        this.user = user;
    }
}