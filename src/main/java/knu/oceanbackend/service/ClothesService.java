package knu.oceanbackend.service;

import knu.oceanbackend.dto.clothes.ClothesCreateRequestDto;
import knu.oceanbackend.dto.clothes.ClothesResponseDto;
import knu.oceanbackend.dto.clothes.ClothesUpdateRequestDto;
import knu.oceanbackend.entity.Clothes;
import knu.oceanbackend.entity.Post;
import knu.oceanbackend.entity.User;
import knu.oceanbackend.exception.ClothesNotFoundException;
import knu.oceanbackend.exception.PostNotFoundException;
import knu.oceanbackend.exception.UserNotFoundException;
import knu.oceanbackend.external.AiServerClient;
import knu.oceanbackend.repository.ClothesRepository;
import knu.oceanbackend.repository.PostRepository;
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
    private final PostRepository postRepository;
    String directoryPath = "src/main/resources/static/clothes";

    private final AiServerClient aiServerClient;

    public void processOriginalClothesImage(MultipartFile image, Long userId, Long postId) {
        aiServerClient.sendImageToAi(image, userId, postId);
    }

    public void saveClothes(MultipartFile image, ClothesCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = null;
        if (requestDto.getPostId() != null){
            post = postRepository.findById(requestDto.getPostId())
                    .orElseThrow(() -> new PostNotFoundException("Post not found"));
        }

        String filename = UUID.randomUUID() + ".png";
        Path imagePath = Paths.get(directoryPath, filename);

        try {
            Files.createDirectories(Paths.get(directoryPath));
            Files.write(imagePath, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }

        Clothes clothes = new Clothes();

        clothes.setUser(user);
        clothes.setPost(post);
        clothes.setType(requestDto.getType());
        clothes.setDetail(requestDto.getDetail());
        clothes.setPrint(requestDto.getPrint());
        clothes.setTexture(requestDto.getTexture());
        clothes.setStyle(requestDto.getStyle());
        clothes.setImageSrc("/clothes/" + filename);

        clothesRepository.save(clothes);
    }

    public ClothesResponseDto getClothById(Long id) {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new ClothesNotFoundException("Clothes not found"));
        return ClothesResponseDto.builder()
                .username(clothes.getUser().getUsername())
                .name(clothes.getName())
                .print(clothes.getPrint())
                .texture(clothes.getTexture())
                .style(clothes.getStyle())
                .detail(clothes.getDetail())
                .type(clothes.getType())
                .imageSrc(clothes.getImageSrc())
                .build();
    }

    public List<Clothes> getClothesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return clothesRepository.findByUser(user);
    }

    public void updateCloth(Long id, ClothesUpdateRequestDto requestDto) {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new ClothesNotFoundException("Clothes not found"));
        if (requestDto.getName() != null) clothes.setName(requestDto.getName());
        if (requestDto.getPrint() != null) clothes.setPrint(requestDto.getPrint());
        if (requestDto.getTexture() != null) clothes.setTexture(requestDto.getTexture());
        if (requestDto.getStyle() != null) clothes.setStyle(requestDto.getStyle());
        if (requestDto.getDetail() != null) clothes.setDetail(requestDto.getDetail());
        if (requestDto.getType() != null) clothes.setType(requestDto.getType());
        clothesRepository.save(clothes);
    }

    public void deleteCloth(Long id) {
        Clothes clothes = clothesRepository.findById(id)
                .orElseThrow(() -> new ClothesNotFoundException("Clothes not found"));
        clothesRepository.deleteById(id);
    }
} 