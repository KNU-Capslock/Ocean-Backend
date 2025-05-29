package knu.oceanbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import knu.oceanbackend.dto.post.PostRequestDto;
import knu.oceanbackend.dto.post.PostResponseDto;
import knu.oceanbackend.entity.Post;
import knu.oceanbackend.service.ClothesService;
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
    private final ClothesService clothesService;

    private final Path uploadDir = Paths.get("src/main/resources/static/posts");

    @Operation(
            summary = "게시물 생성",
            description = "OOTD 사진을 넣으면 원본 저장 및 자동으로 AI 서버 전송 후 사진 저장"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createPost(
            HttpServletRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("post") PostRequestDto requestDto) {
        Long userId = (Long) request.getAttribute("user_id");
        String filename = null;
        if (image != null && !image.isEmpty()) {
            try {
                filename = UUID.randomUUID() + ".png";

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
        Long postId = postService.createPost(userId, post);
        clothesService.processOriginalClothesImage(image, userId, postId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "게시물 단건 조회",
            description = "게시물 id를 사용하여 단건 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(
            summary = "특정 사용자 게시물 조회",
            description = "특정 사용자가 생성한 게시물 모두 조회(수정 날짜 내림차순)"
    )
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("user_id");
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @Operation(
            summary = "전체 게시물 조회",
            description = "모든 게시물을 조회(수정 날짜 내림차순)"
    )
    @GetMapping("/all")
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @Operation(
            summary = "게시물 수정",
            description = "title과 content 수정 가능, 이미지 수정 불가능"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto) {

        postService.updatePost(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "게시물 삭제",
            description = "게시물 id를 통해 삭제"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
} 