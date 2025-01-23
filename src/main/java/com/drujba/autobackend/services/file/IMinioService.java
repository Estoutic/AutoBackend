package com.drujba.autobackend.services.file;

import java.io.InputStream;

public interface IMinioService {

    String uploadFile(String fileName, InputStream inputStream, String contentType);

    String uploadReport(String fileName, InputStream inputStream, String contentType);

    void deleteFile(String fileName);
}
