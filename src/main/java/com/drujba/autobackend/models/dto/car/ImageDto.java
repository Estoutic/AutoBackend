package com.drujba.autobackend.models.dto.car;

import com.drujba.autobackend.db.entities.car.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {

    private UUID id;
    private String path;

    public ImageDto(Image image) {
        this.id = image.getId();
        this.path = image.getFilePath();
    }

}
