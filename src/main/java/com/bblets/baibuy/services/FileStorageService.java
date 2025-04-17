package com.bblets.baibuy.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private final Path rootLocation;
    private static final String PRODUCT_UPLOAD_SUBDIR = "public/uploads/products/";
    private static final String PRODUCT_URL_PREFIX = "/uploads/products/";

    public FileStorageService() {
        this.rootLocation = Paths.get(PRODUCT_UPLOAD_SUBDIR);
        init();
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
            log.info("Initialized product image storage directory: {}", rootLocation.toAbsolutePath());
        } catch (IOException e) {
            log.error("Could not initialize product image storage at {}", rootLocation.toAbsolutePath(), e);
        }
    }

    /**
     * Stores multiple image files.
     *
     * @param files List of MultipartFile objects to store.
     * @return List of relative URL paths for the stored files. Returns empty list
     *         if input is null/empty.
     * @throws IOException if any file fails to store.
     */
    public List<String> storeFiles(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> savedUrls = new ArrayList<>();
        List<MultipartFile> validFiles = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .collect(Collectors.toList());

        if (validFiles.isEmpty()) {
            log.warn("No valid files provided for storage.");
            return Collections.emptyList();
        }

        for (MultipartFile file : validFiles) {
            savedUrls.add(storeSingleFile(file));
        }
        return savedUrls;
    }

    /**
     * Stores a single image file.
     *
     * @param file The MultipartFile to store.
     * @return The relative URL path of the stored file.
     * @throws IOException           if the file is empty or cannot be stored.
     * @throws IllegalStateException if the file cannot be stored.
     */
    private String storeSingleFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Cannot store an empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // Sanitize filename part if needed, keeping UUID + extension is safest
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        Path destinationFile = this.rootLocation.resolve(uniqueFileName).normalize().toAbsolutePath();

        // Ensure file path doesn't go outside intended directory
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            log.error("Security risk: Cannot store file outside current directory. Attempted path: {}",
                    destinationFile);
            throw new IllegalStateException("Cannot store file outside current directory.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored file: {} as {}", originalFilename, uniqueFileName);
            return PRODUCT_URL_PREFIX + uniqueFileName;
        } catch (IOException e) {
            log.error("Failed to store file {}: {}", originalFilename, e.getMessage(), e);
            throw new IOException("Failed to store file " + originalFilename, e);
        }
    }

    /**
     * Deletes files associated with the given relative URLs.
     *
     * @param fileUrls List of relative URL paths (e.g.,
     *                 /uploads/products/uuid.jpg).
     */
    public void deleteFiles(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) {
            return;
        }

        for (String url : fileUrls) {
            if (url != null && url.startsWith(PRODUCT_URL_PREFIX)) {
                try {
                    String filename = url.substring(PRODUCT_URL_PREFIX.length());
                    Path filePath = rootLocation.resolve(filename).normalize();
                    // Security check before deletion
                    if (Files.exists(filePath) && filePath.startsWith(rootLocation.toAbsolutePath())) {
                        Files.delete(filePath);
                        log.info("Deleted file: {}", filePath);
                    } else {
                        log.warn("File not found or path issue, cannot delete: {}", url);
                    }
                } catch (IOException e) {
                    log.error("Failed to delete file for URL {}: {}", url, e.getMessage(), e);
                } catch (Exception e) {
                    log.error("Unexpected error deleting file for URL {}: {}", url, e.getMessage(), e);
                }
            } else {
                log.warn("Skipping deletion for invalid or non-product URL: {}", url);
            }
        }
    }
}