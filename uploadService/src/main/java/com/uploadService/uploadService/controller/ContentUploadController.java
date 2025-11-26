package com.uploadService.uploadService.controller;


import com.uploadService.uploadService.services.ContentUploadServices;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class ContentUploadController {

    @Autowired
    private ContentUploadServices contentUploadServices;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadcontent(@RequestParam MultipartFile file){


        try{
            String s = contentUploadServices.saveVideo(file);
        return ResponseEntity.status(HttpStatus.OK).body(s);
        }catch (Exception e){
            e.getMessage();
            throw new RuntimeException("content upload fail" + e.getMessage());
        }


    }


}
