package com.example.seriesService.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "content")
public class ContentEntity {
    @Id
    @Builder.Default
    String contentId = UUID.randomUUID().toString();

    @Indexed
    @NotBlank(message = "path is required")
    String path;
}
