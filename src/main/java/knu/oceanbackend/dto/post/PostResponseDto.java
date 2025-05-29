package knu.oceanbackend.dto.post;

import knu.oceanbackend.dto.clothes.ClothesResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageSrc;
    private LocalDateTime createdAt;
    private String username;

    private List<ClothesResponseDto> clothesList;
}
