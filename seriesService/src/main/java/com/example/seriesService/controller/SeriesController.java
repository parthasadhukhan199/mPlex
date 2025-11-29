package com.example.seriesService.controller;


import com.example.seriesService.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @GetMapping("/getByTitle/{title}")
    public ResponseEntity<?> getByTitle(@PathVariable String title){
        return ResponseEntity.status(HttpStatus.OK).body(seriesService.getSeries(title));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getByTitle(){
        return ResponseEntity.status(HttpStatus.OK).body(seriesService.getAllSeries());
    }

    @GetMapping("/getByGenre/{genre}")
    public ResponseEntity<?> getByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(seriesService.getByGenre(genre));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        return  ResponseEntity.ok(seriesService.getById(id));
    }

}
