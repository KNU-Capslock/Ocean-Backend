package knu.oceanbackend.repository;

import knu.oceanbackend.entity.Clothes;
import knu.oceanbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findByUser(User user);
}