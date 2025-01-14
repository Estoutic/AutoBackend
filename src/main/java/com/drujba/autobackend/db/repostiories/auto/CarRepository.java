package com.drujba.autobackend.db.repostiories.auto;

import com.drujba.autobackend.db.entities.auto.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
}
