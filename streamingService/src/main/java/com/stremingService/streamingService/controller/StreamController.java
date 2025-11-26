package com.stremingService.streamingService.controller;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;



@RestController
@RequestMapping("/api/v1/stream")
public class StreamController {

    @Autowired
    private MinioClient minioClient;

    String bucket = "content";

    @GetMapping("/{videoId}/master.m3u8")
    public ResponseEntity<?> streamMaster(@PathVariable String videoId) {
        try {
            String objectName = videoId + "/master.m3u8";

            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"))
                    .body(new InputStreamResource(stream));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{videoId}/{tsFile}")
    public ResponseEntity<?> streamTs(
            @PathVariable String videoId,
            @PathVariable String tsFile) {

        try {
            String objectName = videoId + "/" + tsFile;

            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp2t"))
                    .body(new InputStreamResource(stream));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}

