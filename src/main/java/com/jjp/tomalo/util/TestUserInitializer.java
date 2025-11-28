package com.jjp.tomalo.util;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.match.*; // CompatibilityProfile, IdealTypeFilter 등 import
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
@Profile("!test")
public class TestUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 5) {
            return;
        }

        List<Favorite> allFavorites = favoriteRepository.findAll();
        if (allFavorites.isEmpty()) {
            return;
        }

        Random random = new Random();

        // 1. 랜덤 데이터 배열 정의
        String[] places = {"서울", "경기", "인천", "부산", "대구", "광주", "대전", "울산", "세종"};
        String[] jobs = {"개발자", "디자이너", "기획자", "마케터", "학생", "의사", "변호사", "교사", "프리랜서", "공무원"};
        String[] educations = {"고등학교 졸업", "대학교 재학", "대학교 졸업", "석사 이상", "박사"};
        String[] mbtis = {"ISTJ", "ISFJ", "INFJ", "INTJ", "ISTP", "ISFP", "INFP", "INTP", "ESTP", "ESFP", "ENFP", "ENTP", "ESTJ", "ESFJ", "ENFJ", "ENTJ"};

        // ✨ [추가] 라이프스타일 옵션
        String[] religions = {"무교", "기독교", "불교",  "기타"};
        String[] drinkings = {"안 마셔요", "가끔 마셔요", "즐겨 마셔요"};

        // ✨ [추가] 이상형 필터 옵션 (상관없음 포함)
        String[] religionPrefs = {"상관없음", "기독교", "불교","무교"};
        String[] drinkingPrefs = {"상관없음", "안 마셔요", "가끔 마셔요", "즐겨 마셔요"};

        for (int i = 1; i <= 100; i++) {
            // --- 1. User 생성 ---
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

            // --- 2. Profile 생성 (라이프스타일 정보 포함) ---
            long minDay = LocalDate.of(1988, 1, 1).toEpochDay(); // 대략 30대 중반까지
            long maxDay = LocalDate.of(2004, 12, 31).toEpochDay(); // 대략 20대 초반까지
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);

            com.jjp.tomalo.domain.profiles.Profile profile = com.jjp.tomalo.domain.profiles.Profile.builder()
                    .nickname("유저" + i)
                    .gender(i % 2 == 0 ? Gender.FEMALE : Gender.MALE) // 남녀 성비 5:5로 맞춤
                    .birthday(LocalDate.ofEpochDay(randomDay))
                    .place(places[random.nextInt(places.length)])
                    .selfIntroduction("안녕하세요, 좋은 인연 찾고 싶어요. " + i)
                    .job(jobs[random.nextInt(jobs.length)])
                    .education(educations[random.nextInt(educations.length)])
                    .mbti(mbtis[random.nextInt(mbtis.length)])
                    .height(150 + random.nextInt(41))
                    .income(IncomeRange.values()[random.nextInt(IncomeRange.values().length)])
                    .user(newUser)
                    .opt(true) // 매칭 참여 ON

                    // ✨ [추가] 나의 실제 라이프스타일
                    .isSmoker(random.nextBoolean()) // true or false
                    .religion(religions[random.nextInt(religions.length)])
                    .drinking(drinkings[random.nextInt(drinkings.length)])
                    .build();

            // --- 3. CompatibilityProfile (가치관) 생성 및 설정 ---
            CompatibilityProfile compatibilityProfile = CompatibilityProfile.builder()
                    .lifePriority(LifePriority.values()[random.nextInt(LifePriority.values().length)])
                    .weekendStyle(WeekendStyle.values()[random.nextInt(WeekendStyle.values().length)])
                    .conflictStyle(ConflictStyle.values()[random.nextInt(ConflictStyle.values().length)])
                    .loveLanguage(LoveLanguage.values()[random.nextInt(LoveLanguage.values().length)])
                    .build();

            profile.setCompatibilityProfile(compatibilityProfile); // Profile에 설정

            // --- 4. ✨ [추가] IdealTypeFilter (이상형) 생성 및 설정 ---
            int minAge = 20 + random.nextInt(5); // 20~24세
            IdealTypeFilter idealTypeFilter = IdealTypeFilter.builder()
                    .idealAnimalFace(AnimalFace.values()[random.nextInt(AnimalFace.values().length)])
                    .minAge(minAge)
                    .maxAge(minAge + 5 + random.nextInt(10)) // minAge보다 5~14살 많게 설정
                    .maxDistance((random.nextInt(10) + 1) * 10) // 10km ~ 100km
                    .preferSmoker(random.nextBoolean() ? false : null) // false(비흡연 선호) 또는 null(상관없음)
                    .religionPreference(religionPrefs[random.nextInt(religionPrefs.length)])
                    .drinkingPreference(drinkingPrefs[random.nextInt(drinkingPrefs.length)])
                    // .minHeight, .minIncome 등 필요시 추가
                    .build();

            profile.setIdealTypeFilter(idealTypeFilter); // Profile에 설정

            // --- 5. ProfileImage 추가 ---
            int imageCount = 1 + random.nextInt(3);
            for (int j = 1; j <= imageCount; j++) {
                // 남녀 구분에 따라 랜덤 이미지 URL 다르게 (예시용 picsum 설정)
                String imgId = (i % 2 == 0) ? "woman" : "man";
                ProfileImage image = ProfileImage.builder()
                        // ⚠️ 실제 S3 키가 아니므로 프론트에서 깨질 수 있음. 테스트용 placeholder 이미지
                        .imageUrl("https://picsum.photos/seed/" + imgId + i + j + "/400/600")
                        .displayOrder(j)
                        .build();
                profile.addImage(image);
            }

            // --- 6. ProfileFavorite 추가 ---
            Collections.shuffle(allFavorites);
            int favoriteCount = 2 + random.nextInt(4);
            for (int k = 0; k < favoriteCount; k++) {
                if (k < allFavorites.size()) {
                    ProfileFavorite profileFavorite = new ProfileFavorite(allFavorites.get(k));
                    profile.addFavorite(profileFavorite);
                }
            }

            // --- 7. 저장 (Cascade로 인해 Profile, IdealType, Compatibility 등 자동 저장) ---
            newUser.setProfile(profile);
            userRepository.save(newUser);
        }
    }
}