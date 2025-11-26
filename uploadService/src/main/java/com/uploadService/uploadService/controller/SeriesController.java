package com.uploadService.uploadService.controller;

import com.uploadService.uploadService.entity.SeriesEntity;
import com.uploadService.uploadService.model.SeriesRequestModel;
import com.uploadService.uploadService.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;


    @PostMapping("/create")
    public ResponseEntity<?> createSeries(@ModelAttribute SeriesRequestModel request) {
        SeriesEntity created = seriesService.createSeries(request);
        return ResponseEntity.ok(created);
    }

    @PostMapping(value = "/{seriesId}/add-content")
    public ResponseEntity<?> addContentToSeries(
            @PathVariable String seriesId,
            @RequestPart("file") MultipartFile videoFile
    ) {
        SeriesEntity updated = seriesService.addContentToSeries(seriesId, videoFile);
        return ResponseEntity.ok(updated);
    }

}
