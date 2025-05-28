package knu.oceanbackend.service;

import knu.oceanbackend.entity.Clothes;
import knu.oceanbackend.entity.User;
import knu.oceanbackend.exception.UserNotFoundException;
import knu.oceanbackend.repository.ClothesRepository;
import knu.oceanbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClothesService {
    private final UserRepository userRepository;
    private final ClothesRepository clothesRepository;
    private final UserService userService;

    public Clothes createCloth(Clothes cloth, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        cloth.setUser(user);
        return clothesRepository.save(cloth);
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