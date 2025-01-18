package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.models.dto.car.ImageResponseDto;
import com.drujba.autobackend.models.dto.car.UploadImageRequest;
import com.drujba.autobackend.services.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}/all")
    public ResponseEntity<ImageResponseDto> getAllImages(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(imageService.getImagesByCarId(id));
    }

    @DeleteMapping("/{id}/photo/{photoId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") UUID id, @PathVariable("photoId") UUID photoId) {
        imageService.deleteImage(id, photoId);
        return ResponseEntity.noContent().build();
    }
}

