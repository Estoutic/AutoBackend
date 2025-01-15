package com.drujba.autobackend.db.entities.translation;

import com.drujba.autobackend.db.entities.car.CarModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "car_model_translations")
public class CarModelTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModel carModel;

    private String locale;

    private String brand;

    private String model;

    private String generation;
}
