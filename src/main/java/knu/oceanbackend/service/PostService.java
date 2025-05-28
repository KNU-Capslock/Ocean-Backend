package knu.oceanbackend.service;

import knu.oceanbackend.entity.Clothes;
import knu.oceanbackend.entity.Post;
import knu.oceanbackend.entity.User;
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
    private final UserService userService;
    private final ClothesService clothService;
    private final UserRepository userRepository;

    public Post createPost(Post post, Long userId, List<Long> clothIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);
        
        if (clothIds != null && !clothIds.isEmpty()) {
            List<Clothes> clothes = clothIds.stream()
                    .map(clothService::getClothById)
                    .toList();
            post.getClothes().addAll(clothes);
        }
        
        return postRepository.save(post);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<Post> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Post updatePost(Long id, Post postDetails, List<Long> clothIds) {
        Post post = getPostById(id);
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        
        if (clothIds != null) {
            post.getClothes().clear();
            List<Clothes> clothes = clothIds.stream()
                    .map(clothService::getClothById)
                    .toList();
            post.getClothes().addAll(clothes);
        }
        
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
} 