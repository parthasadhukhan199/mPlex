package com.example.seriesService.repo;


import com.example.seriesService.entity.SeriesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends MongoRepository<SeriesEntity,String> {
    List<SeriesEntity> findByGenreIgnoreCase(String genre);

    List<SeriesEntity> findByTitleContainingIgnoreCase(String titlePart);
}
