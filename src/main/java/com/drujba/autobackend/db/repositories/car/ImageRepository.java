package com.drujba.autobackend.db.repositories.car;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    List<Image> findByCar(Car car);

}
