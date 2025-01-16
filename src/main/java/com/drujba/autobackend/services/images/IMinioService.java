package com.drujba.autobackend.services.images;

import java.io.InputStream;

public interface IMinioService {

    String uploadFile(String fileName, InputStream inputStream, String contentType);
}
