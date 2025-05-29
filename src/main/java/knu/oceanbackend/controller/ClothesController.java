package knu.oceanbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import knu.oceanbackend.dto.clothes.ClothesCreateRequestDto;
import knu.oceanbackend.dto.clothes.ClothesResponseDto;
import knu.oceanbackend.dto.clothes.ClothesUpdateRequestDto;
import knu.oceanbackend.entity.Clothes;
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

    @PostMapping(value = "/original", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadOriginalClothes(
            HttpServletRequest request,
            @RequestPart("image") MultipartFile image) {
        Long userId = (Long) request.getAttribute("user_id");
        clothesService.processOriginalClothesImage(image, userId, null);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveClothes(
            @RequestPart("file") MultipartFile image,
            @RequestPart("data") ClothesCreateRequestDto requestDto
    ) {
        clothesService.saveClothes(image, requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Clothes>> getClothesByUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("user_id");
        return ResponseEntity.ok(clothesService.getClothesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothesResponseDto> getClothById(@PathVariable Long id) {
        return ResponseEntity.ok(clothesService.getClothById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Clothes> updateCloth(@PathVariable Long id, @RequestBody ClothesUpdateRequestDto requestDto) {
        clothesService.updateCloth(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCloth(@PathVariable Long id) {
        clothesService.deleteCloth(id);
        return ResponseEntity.ok().build();
    }
} 