package com.drujba.autobackend.db.entities.auto;

import com.drujba.autobackend.models.enums.auto.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModel carModel;

    private int year;
    private String color;
    private int mileage;
    private int ownersCount;

    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    private int enginePower;

    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    private double engineCapacity;

    @Enumerated(EnumType.STRING)
    private SteeringPosition steeringPosition;

    private int seatsCount;
    private double price;
    private String description;

    @Column(name = "is_available")
    private boolean isAvailable;

    @CreationTimestamp
    private Instant createdAt;
}
