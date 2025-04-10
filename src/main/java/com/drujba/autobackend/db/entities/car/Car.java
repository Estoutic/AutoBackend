package com.drujba.autobackend.db.entities.car;

import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.enums.car.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cars", indexes = {
        @Index(name = "idx_car_model_id", columnList = "car_model_id"),
        @Index(name = "idx_car_price", columnList = "price"),
        @Index(name = "idx_car_year", columnList = "year"),
        @Index(name = "idx_car_available", columnList = "is_available")
})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModel carModel;

    private int year;

    private String color;

    private BigDecimal mileage;

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

    private BigDecimal price;

    private String description;

    @Column(nullable = false, unique = true, length = 17)
    private String vin;

    @Column(name = "is_available")
    private boolean isAvailable;

    @CreationTimestamp
    private Instant createdAt;

    public Car(CarCreationDto dto, CarModel carModel) {
        if (dto.getVin() != null & !Objects.equals(dto.getVin(), "")) {
            this.vin = dto.getVin();
        }
        this.carModel = carModel;
        this.year = dto.getYear();
        this.color = dto.getColor();
        this.mileage = dto.getMileage() != null ? dto.getMileage() : BigDecimal.ZERO;
        this.ownersCount = dto.getOwnersCount() != null ? dto.getOwnersCount() : 0;
        this.transmissionType = dto.getTransmissionType();
        this.bodyType = dto.getBodyType();
        this.enginePower = dto.getEnginePower() != null ? Integer.parseInt(dto.getEnginePower()) : 0;
        this.engineType = dto.getEngineType();
        this.driveType = dto.getDriveType();
        this.engineCapacity = dto.getEngineCapacity() != null ? Double.parseDouble(dto.getEngineCapacity()) : 0.0;
        this.steeringPosition = dto.getSteeringPosition();
        this.seatsCount = dto.getSeatsCount() != null ? dto.getSeatsCount() : 0;
        this.price = dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO;
        this.description = dto.getDescription();
        this.isAvailable = true;
    }
}
