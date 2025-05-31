package knu.oceanbackend.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final String imageBasePath = "images/";

    @GetMapping("/posts/{filename:.+}")
    public ResponseEntity<Resource> getPostImage(@PathVariable String filename) {
        return serveImage("posts", filename);
    }

    @GetMapping("/clothes/{filename:.+}")
    public ResponseEntity<Resource> getClothesImage(@PathVariable String filename) {
        return serveImage("clothes", filename);
    }

    private ResponseEntity<Resource> serveImage(String folder, String filename) {
        try {
            Path filePath = Paths.get(imageBasePath + folder + "/" + filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}