package com.jjp.tomalo.util;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.profiles.*;
import com.jjp.tomalo.repository.FavoriteRepository;
import com.jjp.tomalo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Profile("!test") // 테스트 환경에서는 실행되지 않도록 설정
public class TestUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // DB에 유저가 이미 있으면 실행하지 않음
        if (userRepository.count() > 5) {
            return;
        }

        List<Favorite> allFavorites = favoriteRepository.findAll();
        if (allFavorites.isEmpty()) {
            return; // 관심사 데이터가 없으면 중단
        }

        Random random = new Random();

        String[] places = {"서울", "경기", "인천", "부산", "대구", "광주", "대전", "울산", "세종"};
        String[] jobs = {"개발자", "디자이너", "기획자", "마케터", "학생", "의사", "변호사", "교사", "프리랜서"};
        String[] educations = {"고등학교 졸업", "대학교 재학", "대학교 졸업", "석사 이상"};
        String[] mbtis = {"ISTJ", "ISFJ", "INFJ", "INTJ", "ISTP", "ISFP", "INFP", "INTP", "ESTP", "ESFP", "ENFP", "ENTP", "ESTJ", "ESFJ", "ENFJ", "ENTJ"};

        for (int i = 1; i <= 100; i++) {
            // 1. User 생성
            User newUser = User.builder()
                    .email("testuser" + i + "@example.com")
                    .phoneNumber("010" + String.format("%08d", i))
                    .password(passwordEncoder.encode("password"))
                    .provider("local")
                    .name("테스트유저" + i)
                    .privacyPolicyAgreed(true)
                    .marketingEmailOptIn(false)
                    .marketingSmsOptIn(false)
                    .build();

            // 2. Profile 생성
            long minDay = LocalDate.of(1984, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2004, 12, 31).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);

            com.jjp.tomalo.domain.profiles.Profile profile = com.jjp.tomalo.domain.profiles.Profile.builder()
                    .nickname("테스트닉네임" + i)
                    .gender(random.nextBoolean() ? Gender.MALE : Gender.FEMALE)
                    .birthday(LocalDate.ofEpochDay(randomDay))
                    .place(places[random.nextInt(places.length)])
                    .selfIntroduction("안녕하세요, 테스트 유저 " + i + "입니다.")
                    .job(jobs[random.nextInt(jobs.length)])
                    .education(educations[random.nextInt(educations.length)])
                    .mbti(mbtis[random.nextInt(mbtis.length)])
                    .height(150 + random.nextInt(41)) // 150 ~ 190
                    .income(IncomeRange.values()[random.nextInt(IncomeRange.values().length)])
                    .user(newUser)
                    .opt(true)
                    .build();

            // 3. ProfileImage 추가 (1~3개)
            int imageCount = 1 + random.nextInt(3);
            for (int j = 1; j <= imageCount; j++) {
                ProfileImage image = ProfileImage.builder()
                        .imageUrl("https://picsum.photos/400/600?random=" + i + "_" + j)
                        .displayOrder(j)
                        .build();
                profile.addImage(image);
            }

            // 4. ProfileFavorite 추가 (2~5개)
            Collections.shuffle(allFavorites);
            int favoriteCount = 2 + random.nextInt(4);
            for (int k = 0; k < favoriteCount; k++) {
                if (k < allFavorites.size()) {
                    ProfileFavorite profileFavorite = new ProfileFavorite(allFavorites.get(k));
                    profile.addFavorite(profileFavorite);
                }
            }

            // 5. User에 Profile 설정 및 저장
            newUser.setProfile(profile);
            userRepository.save(newUser);
        }
    }
}