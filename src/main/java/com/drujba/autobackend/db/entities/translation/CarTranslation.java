package com.drujba.autobackend.db.entities.translation;


import com.drujba.autobackend.db.entities.auto.Car;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "car_translations")
public class CarTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    private String locale;

    private String color;

    private String description;

    private int mileage;

    private double price;
}
