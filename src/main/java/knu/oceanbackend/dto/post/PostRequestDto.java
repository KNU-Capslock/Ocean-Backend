package knu.oceanbackend.dto.post;

import knu.oceanbackend.entity.Post;
import lombok.Data;

@Data
public class PostRequestDto {
    private String title;
    private String content;

    public Post toEntity(String imageSrc) {
        Post post = new Post();
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setImageSrc(imageSrc);
        return post;
    }
}
