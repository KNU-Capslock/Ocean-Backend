package knu.oceanbackend.repository;

import knu.oceanbackend.entity.Post;
import knu.oceanbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByCreatedAtDesc(User user);

    List<Post> findAllByOrderByUpdatedAtDesc();
}