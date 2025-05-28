package knu.oceanbackend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity { // 다른 곳에서 인스턴스화 해서 사용하지 못하게 추상 클래스로 선언

    @Schema(name = "created_at")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Schema(name = "modified_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
