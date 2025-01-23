package com.drujba.autobackend.services.file.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.Image;
import com.drujba.autobackend.db.repositories.car.CarRepository;
import com.drujba.autobackend.db.repositories.car.ImageRepository;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.exceptions.car.ImageDoestNotExistException;
import com.drujba.autobackend.models.dto.car.ImageDto;
import com.drujba.autobackend.models.dto.car.ImageResponseDto;
import com.drujba.autobackend.models.enums.BucketType;
import com.drujba.autobackend.services.file.IImageService;
import com.drujba.autobackend.services.file.IMinioService;
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
        String path = minioService.uploadFile(BucketType.IMAGE, image.getId().toString(), inputStream, contentType);
        image.setFilePath(String.format("%s%s", path, image.getId()));
        return imageRepository.save(image).getId();
    }

    @Override
    public ImageResponseDto getImagesByCarId(UUID carId) {

        Car car = carRepository.findById(carId).orElseThrow(() -> new CarDoesNotExistException(carId.toString()));

        ImageResponseDto imageResponseDto = new ImageResponseDto();
        imageResponseDto.setImages(imageRepository.findByCar(car).stream()
                .map(image -> new ImageDto(image))
                .toList());

        return imageResponseDto;
    }

    @Override
    public void deleteImage(UUID carId, UUID imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageDoestNotExistException(imageId.toString()));

        if (!image.getCar().getId().equals(carId)) {
            throw new RuntimeException("Image does not belong to the specified car");
        }
        String fileName = image.getFilePath().substring(image.getFilePath().lastIndexOf('/') + 1);
        minioService.deleteFile(BucketType.IMAGE, fileName);
        imageRepository.delete(image);
    }
}
