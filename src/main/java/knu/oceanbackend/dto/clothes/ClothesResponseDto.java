package knu.oceanbackend.dto.clothes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClothesResponseDto {
    private Long id;
    private String username;
    private String name;
    private String type;
    private String detail;
    private String print;
    private String texture;
    private String style;
    private String imageSrc;
}
