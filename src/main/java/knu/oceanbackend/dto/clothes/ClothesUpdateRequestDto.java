package knu.oceanbackend.dto.clothes;

import lombok.Data;

@Data
public class ClothesUpdateRequestDto {
    private String name;
    private String type;
    private String detail;
    private String print;
    private String texture;
    private String style;
}
