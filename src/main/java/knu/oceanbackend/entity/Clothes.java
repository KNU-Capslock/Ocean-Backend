package knu.oceanbackend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Clothes extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private String type;
    private String detail;
    private String print;
    private String texture;
    private String style;

    @Column(nullable = false)
    @Schema(name = "image_src")
    private String imageSrc;

    @Schema(name = "created_at")
    private LocalDateTime createdAt;

    @Schema(name = "modified_at")
    private LocalDateTime modifiedAt;
} 