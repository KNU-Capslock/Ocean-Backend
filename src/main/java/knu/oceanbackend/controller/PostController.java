package knu.oceanbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import knu.oceanbackend.dto.post.PostRequestDto;
import knu.oceanbackend.dto.post.PostResponseDto;
import knu.oceanbackend.entity.Post;
import knu.oceanbackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController{
    private final PostService postService;
    private final Path uploadDir = Paths.get("src/main/resources/static/posts");

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPost(
            HttpServletRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("post") PostRequestDto requestDto) {
        Long userId = (Long) request.getAttribute("user_id");
        String filename = null;
        if (image != null && !image.isEmpty()) {
            try {
                filename = UUID.randomUUID() + "_" + image.getOriginalFilename();

                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path filePath = uploadDir.resolve(filename);
                image.transferTo(filePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Post post = requestDto.toEntity(filename);
        postService.createPost(userId, post);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("user_id");
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto) {

        postService.updatePost(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
} 