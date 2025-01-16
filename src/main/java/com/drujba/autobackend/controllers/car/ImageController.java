package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.models.dto.car.UploadImageRequest;
import com.drujba.autobackend.services.images.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/car/image")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/{id}")
    public ResponseEntity<UUID> uploadImage(@PathVariable("id") UUID id,
                                            @ModelAttribute UploadImageRequest request) {
        try {
            return ResponseEntity.ok(imageService.saveImage(id,
                    request.getFile().getOriginalFilename(),
                    request.getFile().getInputStream(),
                    request.getFile().getContentType()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

