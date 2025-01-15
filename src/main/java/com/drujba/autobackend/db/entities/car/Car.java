package com.drujba.autobackend.db.entities.car;

import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.enums.auto.*;
import jakarta.persistence.*;
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

    public Car(CarCreationDto dto, CarModel carModel) {
        this.carModel = carModel;
        this.year = dto.getYear();
        this.color = dto.getColor();
        this.mileage = dto.getMileage() != null ? dto.getMileage().intValue() : 0;
        this.ownersCount = dto.getOwnersCount() != null ? dto.getOwnersCount() : 0;
        this.transmissionType = dto.getTransmissionType();
        this.bodyType = dto.getBodyType();
        this.enginePower = dto.getEnginePower() != null ? Integer.parseInt(dto.getEnginePower()) : 0;
        this.engineType = dto.getEngineType();
        this.driveType = dto.getDriveType();
        this.engineCapacity = dto.getEngineCapacity() != null ? Double.parseDouble(dto.getEngineCapacity()) : 0.0;
        this.steeringPosition = dto.getSteeringPosition();
        this.seatsCount = dto.getSeatsCount() != null ? dto.getSeatsCount() : 0;
        this.price = dto.getPrice() != null ? dto.getPrice().doubleValue() : 0.0;
        this.description = dto.getDescription();
        this.isAvailable = true;
    }
}
