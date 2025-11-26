package com.example.seriesService.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Series")
public class SeriesEntity {

    @Id
    @Builder.Default
    String seriesId = UUID.randomUUID().toString();

    @Indexed
    @NotBlank(message = "Title is required")
    String title;

    String description;

    @Indexed
    @NotEmpty(message = "minimum one genre is  mandatory")
    List<String> genre;

    List<String> contentIds;

    String posterPath;


}
