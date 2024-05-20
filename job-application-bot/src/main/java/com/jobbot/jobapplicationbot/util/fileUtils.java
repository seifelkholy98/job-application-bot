package com.jobbot.jobapplicationbot.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class fileUtils {

    private static final String UPLOAD_DIR = "uploads/";

    public static String saveResume(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }
        String fileName = file.getOriginalFilename();
        Path destinationFile = Paths.get(UPLOAD_DIR).resolve(Paths.get(fileName)).normalize().toAbsolutePath();
        if (!destinationFile.getParent().startsWith(Paths.get(UPLOAD_DIR).toAbsolutePath())) {
            throw new IOException("Cannot store file outside current directory.");
        }
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile);
        }
        return destinationFile.toString();
    }
}
