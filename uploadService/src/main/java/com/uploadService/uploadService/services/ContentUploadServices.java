package com.uploadService.uploadService.services;

import com.uploadService.uploadService.entity.ContentEntity;
import com.uploadService.uploadService.repo.ContentRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ContentUploadServices {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private ContentRepository contentRepository;

    public String saveVideo(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String cleanName = StringUtils.cleanPath(originalName);

            ContentEntity content = new ContentEntity();
            content.setContentId(UUID.randomUUID().toString());

            String bucket = "content";
            String folder = content.getContentId();

            String inputObject = folder + "/" + cleanName;

            // 1 Upload original
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(inputObject)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // 2 Download temp file
            Path tempInput = Files.createTempFile("input-", cleanName);

            try (InputStream in = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(inputObject)
                            .build()
            )) {
                Files.copy(in, tempInput, StandardCopyOption.REPLACE_EXISTING);
            }

            // 3 Convert HLS
            Path tempFolder = Files.createTempDirectory(folder);
            Path hlsOutput = tempFolder.resolve("master.m3u8");

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-i", tempInput.toString(),
                    "-c:v", "libx264",
                    "-c:a", "aac",
                    "-b:v", "2000k",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-start_number", "0",
                    "-f", "hls",
                    hlsOutput.toString()
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) System.out.println("[FFMPEG] " + line);

            int exit = process.waitFor();
            if (exit != 0) throw new RuntimeException("FFmpeg failed");

            // 4 Upload HLS files to MinIO
            for (File f : tempFolder.toFile().listFiles()) {
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(bucket)
                                .object(folder + "/" + f.getName())
                                .filename(f.getAbsolutePath())
                                .build()
                );
            }

            // 5 Save DB path
            content.setPath(bucket + "/" + folder + "/master.m3u8");
            contentRepository.save(content);

            return content.getContentId();

        } catch (Exception e) {
            throw new RuntimeException("Video upload failed: " + e.getMessage());
        }
    }

    public String savePoster(MultipartFile poster) {
        try {
            String originalName = poster.getOriginalFilename();
            String cleanName = StringUtils.cleanPath(originalName);

            String posterId = UUID.randomUUID().toString();
            String bucket = "poster";
            String objectName = posterId + "/" + cleanName;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(poster.getInputStream(), poster.getSize(), -1)
                            .contentType(poster.getContentType())
                            .build()
            );

            return bucket + "/" + objectName;

        } catch (Exception e) {
            throw new RuntimeException("Poster upload failed: " + e.getMessage());
        }
    }
}
