package com.uploadService.uploadService.services;

import com.uploadService.uploadService.entity.SeriesEntity;
import com.uploadService.uploadService.model.SeriesRequestModel;
import com.uploadService.uploadService.repo.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private ContentUploadServices contentUploadServices;

    public SeriesEntity createSeries(SeriesRequestModel request) {

        List<String> contentIds = new ArrayList<>();

        // 1️⃣ Upload each episode video
        for (MultipartFile video : request.getContentFiles()) {
            String contentId = contentUploadServices.saveVideo(video);
            contentIds.add(contentId);
        }

        // 2️⃣ Upload Poster Image
        String posterPath = null;
        if (request.getPosterFile() != null && !request.getPosterFile().isEmpty()) {
            posterPath = contentUploadServices.savePoster(request.getPosterFile());
        }

        // 3️⃣ Create Series Entity
        SeriesEntity series = SeriesEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .contentIds(contentIds)
                .posterPath(posterPath)
                .build();

        // 4️⃣ Save in MongoDB
        return seriesRepository.save(series);
    }

    public SeriesEntity addContentToSeries(String seriesId, MultipartFile videoFile) {
        // 1️⃣ Find existing series
        SeriesEntity series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new RuntimeException("Series not found: " + seriesId));

        // 2️⃣ Upload video using your upload service
        String contentId = contentUploadServices.saveVideo(videoFile);

        // 3️⃣ Add contentId to existing list
        if (series.getContentIds() == null) {
            series.setContentIds(new ArrayList<>());
        }
        series.getContentIds().add(contentId);

        // 4️⃣ Save updated series
        return seriesRepository.save(series);
    }

}
