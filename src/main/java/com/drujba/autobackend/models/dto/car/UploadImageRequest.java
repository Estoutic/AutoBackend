package com.drujba.autobackend.models.dto.car;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadImageRequest {
    private MultipartFile file;
}