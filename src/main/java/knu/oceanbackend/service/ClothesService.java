package knu.oceanbackend.service;

import knu.oceanbackend.dto.AiClothesResult;
import knu.oceanbackend.dto.clothes.ClothesCreateRequestDto;
import knu.oceanbackend.dto.clothes.ClothesResponseDto;
import knu.oceanbackend.entity.Clothes;
import knu.oceanbackend.entity.User;
import knu.oceanbackend.exception.UserNotFoundException;
import knu.oceanbackend.external.AiServerClient;
import knu.oceanbackend.repository.ClothesRepository;
import knu.oceanbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClothesService {
    private final UserRepository userRepository;
    private final ClothesRepository clothesRepository;

    private final AiServerClient aiServerClient;

    public void processOriginalClothesImage(MultipartFile image, Long userId) {
        aiServerClient.sendImageToAi(image, userId);
    }

    public void saveClothes(MultipartFile image, ClothesCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 이미지 저장
        String filename = UUID.randomUUID() + ".png";
        Path imagePath = Paths.get("src/main/resources/static/clothes/" + filename);
        try {
            Files.write(imagePath, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }

        Clothes clothes = new Clothes();
        clothes.setUser(user);
        clothes.setType(requestDto.getType());
        clothes.setDetail(requestDto.getDetail());
        clothes.setPrint(requestDto.getPrint());
        clothes.setTexture(requestDto.getTexture());
        clothes.setStyle(requestDto.getStyle());
        clothes.setImageSrc("/clothes/" + filename);

        clothesRepository.save(clothes);
    }

    public Clothes getClothById(Long id) {
        return clothesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cloth not found"));
    }

    public List<Clothes> getClothesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return clothesRepository.findByUser(user);
    }

    public Clothes updateCloth(Long id, Clothes clothDetails) {
        Clothes cloth = getClothById(id);
        cloth.setName(clothDetails.getName());
        cloth.setType(clothDetails.getType());
        cloth.setDetail(clothDetails.getDetail());
        cloth.setPrint(clothDetails.getPrint());
        cloth.setPrint(clothDetails.getPrint());
        cloth.setTexture(clothDetails.getTexture());
        cloth.setStyle(clothDetails.getStyle());
        return clothesRepository.save(cloth);
    }

    public void deleteCloth(Long id) {
        clothesRepository.deleteById(id);
    }
} 