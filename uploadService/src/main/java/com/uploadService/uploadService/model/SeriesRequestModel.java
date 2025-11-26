package com.uploadService.uploadService.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SeriesRequestModel {
    @NotBlank(message = "Title is required")
    String title;
    String description;
    @NotEmpty(message = "Genre list cannot be empty")
    List<String> genre;

    List<MultipartFile> contentFiles;

    MultipartFile posterFile;
}
