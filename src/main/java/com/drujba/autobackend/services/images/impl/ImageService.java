package com.drujba.autobackend.services.images.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.Image;
import com.drujba.autobackend.db.repostiories.car.CarRepository;
import com.drujba.autobackend.db.repostiories.car.ImageRepository;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.services.images.IImageService;
import com.drujba.autobackend.services.images.IMinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IMinioService minioService;
    private final CarRepository carRepository;

    @Override
    public UUID saveImage(UUID carId, String fileName, InputStream inputStream, String contentType) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarDoesNotExistException(carId.toString()));
        Image image = new Image(car);
        imageRepository.save(image);
        String path = minioService.uploadFile(image.getId().toString(), inputStream, contentType);
        image.setFilePath(String.format("%s%s", path, image.getId()));
        return imageRepository.save(image).getId();
    }
}
