package com.drujba.autobackend.services.file;

import com.drujba.autobackend.models.dto.car.ImageResponseDto;

import java.io.InputStream;
import java.util.UUID;

public interface IImageService {

    UUID saveImage(UUID carId, String fileName, InputStream inputStream, String contentType);

    ImageResponseDto getImagesByCarId(UUID carId);

    void deleteImage(UUID carId, UUID imageId);
}
