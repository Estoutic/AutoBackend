package com.drujba.autobackend.services.file.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.Image;
import com.drujba.autobackend.db.repositories.car.CarRepository;
import com.drujba.autobackend.db.repositories.car.ImageRepository;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.exceptions.car.ImageDoesNotBelongException;
import com.drujba.autobackend.exceptions.car.ImageDoestNotExistException;
import com.drujba.autobackend.exceptions.minio.UploadImageException;
import com.drujba.autobackend.models.dto.car.ImageDto;
import com.drujba.autobackend.models.dto.car.ImageResponseDto;
import com.drujba.autobackend.models.enums.BucketType;
import com.drujba.autobackend.services.file.IImageService;
import com.drujba.autobackend.services.file.IMinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IMinioService minioService;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public UUID saveImage(UUID carId, String fileName, InputStream inputStream, String contentType) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarDoesNotExistException(carId.toString()));
        Image image = new Image(car);
        imageRepository.save(image);
        String path = minioService.uploadFile(BucketType.IMAGE, image.getId().toString(), inputStream, contentType);
        image.setFilePath(path);
        return imageRepository.save(image).getId();
    }

    @Override
    @Transactional
    public List<UUID> saveImages(UUID carId, List<MultipartFile> files) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarDoesNotExistException(carId.toString()));

        if (files.size() > 10) {
            throw new UploadImageException("Нельзя загрузить более 10 изображений за раз.");
        }

        List<UUID> imageIds = new ArrayList<>();

        for (MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                Image image = new Image(car);
                imageRepository.save(image);

                String path = minioService.uploadFile(BucketType.IMAGE, image.getId().toString(), inputStream, file.getContentType());
                image.setFilePath(path);
                imageRepository.save(image);

                imageIds.add(image.getId());
            } catch (IOException e) {
                throw new UploadImageException("Ошибка при загрузке файла: " + file.getOriginalFilename(), e);
            }
        }

        return imageIds;
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
            throw new ImageDoesNotBelongException(imageId.toString());
        }
        String fileName = image.getFilePath().substring(image.getFilePath().lastIndexOf('/') + 1);
        minioService.deleteFile(BucketType.IMAGE, fileName);
        imageRepository.delete(image);
    }
}
