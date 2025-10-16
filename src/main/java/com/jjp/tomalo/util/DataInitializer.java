package com.jjp.tomalo.util;

import com.jjp.tomalo.domain.profiles.Favorite;
import com.jjp.tomalo.domain.profiles.FavoriteCategory;
import com.jjp.tomalo.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FavoriteRepository favoriteRepository;

    @Override
    public void run(String... args) throws Exception {
        Map<String, FavoriteCategory> hobbies = Map.of(
                "음악", FavoriteCategory.CREATIVE,
                "운동", FavoriteCategory.EXTROVERT,
                "문화", FavoriteCategory.CREATIVE,
                "자기계발", FavoriteCategory.INTROVERT,
                "만들기", FavoriteCategory.CREATIVE
        );

        hobbies.forEach((name, category) -> {
            if (favoriteRepository.findByName(name).isEmpty()) {
                favoriteRepository.save(new Favorite(name, category));
            }
        });
    }
}
