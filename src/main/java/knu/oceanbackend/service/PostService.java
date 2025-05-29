package knu.oceanbackend.service;

import knu.oceanbackend.dto.post.PostRequestDto;
import knu.oceanbackend.dto.post.PostResponseDto;
import knu.oceanbackend.entity.Post;
import knu.oceanbackend.entity.User;
import knu.oceanbackend.exception.PostNotFoundException;
import knu.oceanbackend.exception.UserNotFoundException;
import knu.oceanbackend.repository.PostRepository;
import knu.oceanbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ClothesService clothService;

    public void createPost(Long userId, Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        post.setUser(user);
        
        postRepository.save(post);
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        return PostResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .imageSrc(post.getImageSrc())
                .createdAt(post.getCreatedAt())
                .username(post.getUser().getUsername())
                .build();
    }

    public List<PostResponseDto> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Post> postList = postRepository.findByUserOrderByCreatedAtDesc(user);
        if (postList.isEmpty()){
            return List.of();
        }
        return postList.stream()
                .map(post -> PostResponseDto.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imageSrc(post.getImageSrc())
                        .createdAt(post.getCreatedAt())
                        .username(post.getUser().getUsername())
                        .build())
                .toList();
    }

    public void updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        postRepository.delete(post);
    }
} 