package knu.oceanbackend.dto.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDto {
    private String title;
    private String content;
    private String imageSrc;
    private LocalDateTime createdAt;
    private String username;
}
