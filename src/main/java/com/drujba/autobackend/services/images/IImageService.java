package com.drujba.autobackend.services.images;

import java.io.InputStream;
import java.util.UUID;

public interface IImageService {

    UUID saveImage(UUID carId, String fileName, InputStream inputStream, String contentType);
}
