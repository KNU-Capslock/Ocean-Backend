package knu.oceanbackend.controller;

import knu.oceanbackend.entity.Clothes;
import knu.oceanbackend.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clothes")
@RequiredArgsConstructor
public class ClothesController {
    private final ClothesService clothesService;

    @PostMapping("/{userId}")
    public ResponseEntity<Clothes> createCloth(@PathVariable Long userId, @RequestBody Clothes cloth) {
        return ResponseEntity.ok(clothesService.createCloth(cloth, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Clothes>> getClothesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(clothesService.getClothesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clothes> getClothById(@PathVariable Long id) {
        return ResponseEntity.ok(clothesService.getClothById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Clothes> updateCloth(@PathVariable Long id, @RequestBody Clothes cloth) {
        return ResponseEntity.ok(clothesService.updateCloth(id, cloth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCloth(@PathVariable Long id) {
        clothesService.deleteCloth(id);
        return ResponseEntity.ok().build();
    }
} 