package knu.oceanbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import knu.oceanbackend.dto.clothes.ClothesCreateRequestDto;
import knu.oceanbackend.dto.clothes.ClothesResponseDto;
import knu.oceanbackend.dto.clothes.ClothesUpdateRequestDto;
import knu.oceanbackend.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    @Operation(
            summary = "AI 서버가 사용하는 엔드포인트",
            description = "백엔드에서 AI 서버로 사진을 보내면 분리한 옷 사진 데이터를 전송받기 위한 엔드포인트. 프론트 사용 X"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveClothes(
            @RequestPart("file") MultipartFile image,
            @RequestPart("data") ClothesCreateRequestDto requestDto
    ) {
        clothesService.saveClothes(image, requestDto);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "분리된 옷 사진 생성",
            description = "원본 이미지를 보내면 분리된 옷 사진 생성"
    )
    @PostMapping(value = "/original", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadOriginalClothes(
            HttpServletRequest request,
            @RequestPart("image") MultipartFile image) {
        Long userId = (Long) request.getAttribute("user_id");
        clothesService.processOriginalClothesImage(image, userId, null);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "옷 조회",
            description = "현재 로그인한 사용자의 옷 전체 조회"
    )
    @GetMapping
    public ResponseEntity<List<ClothesResponseDto>> getClothesByUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("user_id");
        return ResponseEntity.ok(clothesService.getClothesByUser(userId));
    }

    @Operation(
            summary = "옷 단건 조회",
            description = "옷 id를 사용하여 옷 단건 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClothesResponseDto> getClothById(@PathVariable Long id) {
        return ResponseEntity.ok(clothesService.getClothById(id));
    }

    @Operation(
            summary = "옷 수정",
            description = "이미지 제외 수정 가능, 주로 옷 이름을 붙일 때 사용"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCloth(@PathVariable Long id, @RequestBody ClothesUpdateRequestDto requestDto) {
        clothesService.updateCloth(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "옷 삭제",
            description = "옷 id를 옷 삭제"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCloth(@PathVariable Long id) {
        clothesService.deleteCloth(id);
        return ResponseEntity.ok().build();
    }
} 