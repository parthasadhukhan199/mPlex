package com.example.seriesService.services;


import com.example.seriesService.entity.SeriesEntity;
import com.example.seriesService.repo.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    public List<SeriesEntity> getSeries(String seriesName) {
        return seriesRepository.findByTitleContainingIgnoreCase(seriesName);
    }

    public List<SeriesEntity> getAllSeries() {
        return seriesRepository.findAll();
    }

    public List<SeriesEntity> getByGenre(String genre) {
        return seriesRepository.findByGenreIgnoreCase(genre);
    }



}
