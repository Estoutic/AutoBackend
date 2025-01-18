package com.drujba.autobackend.services.image;

import java.io.InputStream;

public interface IMinioService {

    String uploadFile(String fileName, InputStream inputStream, String contentType);

    void deleteFile(String fileName);
}
