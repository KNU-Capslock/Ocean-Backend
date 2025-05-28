package knu.oceanbackend.controller;

import knu.oceanbackend.entity.Post;
import knu.oceanbackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController{
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody Post post,
            @RequestParam(required = false) List<Long> clothIds) {
        return ResponseEntity.ok(postService.createPost(post, userId, clothIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestBody Post post,
            @RequestParam(required = false) List<Long> clothIds) {
        return ResponseEntity.ok(postService.updatePost(id, post, clothIds));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
} 