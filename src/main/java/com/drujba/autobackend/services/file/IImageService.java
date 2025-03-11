package com.drujba.autobackend.services.file;

import com.drujba.autobackend.models.dto.car.ImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface IImageService {

    UUID saveImage(UUID carId, String fileName, InputStream inputStream, String contentType);

    List<UUID> saveImages(UUID carId, List<MultipartFile> files);

    ImageResponseDto getImagesByCarId(UUID carId);

    void deleteImage(UUID carId, UUID imageId);
}
