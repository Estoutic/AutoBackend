package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.exceptions.minio.UploadImageException;
import com.drujba.autobackend.models.dto.car.ImageResponseDto;
import com.drujba.autobackend.models.dto.car.UploadImageRequest;
import com.drujba.autobackend.services.file.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/car/image")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping("/{id}")
    public ResponseEntity<UUID> uploadImage(@PathVariable("id") UUID id,
                                            @ModelAttribute UploadImageRequest request) {
        try {
            return ResponseEntity.ok(imageService.saveImage(id,
                    request.getFile().getOriginalFilename(),
                    request.getFile().getInputStream(),
                    request.getFile().getContentType()));
        } catch (IOException e) {
            throw new UploadImageException(e);
        }
    }


    @GetMapping("/{id}/all")
    public ResponseEntity<ImageResponseDto> getAllImages(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(imageService.getImagesByCarId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}/photo/{photoId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") UUID id, @PathVariable("photoId") UUID photoId) {
        imageService.deleteImage(id, photoId);
        return ResponseEntity.noContent().build();
    }
}

