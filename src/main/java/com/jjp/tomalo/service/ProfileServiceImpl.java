package com.jjp.tomalo.service;


import com.jjp.tomalo.domain.*;
import com.jjp.tomalo.domain.match.CompatibilityProfile;
import com.jjp.tomalo.domain.match.IdealTypeFilter;
import com.jjp.tomalo.domain.profiles.Favorite;
import com.jjp.tomalo.domain.profiles.Profile;
import com.jjp.tomalo.domain.profiles.ProfileFavorite;
import com.jjp.tomalo.domain.profiles.ProfileImage;
import com.jjp.tomalo.dto.auth.CompatibilityDto;
import com.jjp.tomalo.dto.auth.IdealTypeDto;
import com.jjp.tomalo.dto.auth.ProfileCreateRequestDto;
import com.jjp.tomalo.dto.profile.MyProfileResponse;
import com.jjp.tomalo.repository.FavoriteRepository;
import com.jjp.tomalo.repository.ProfileRepository;
import com.jjp.tomalo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl {
    private final ProfileRepository profileRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void createProfile (User user, ProfileCreateRequestDto requestDto) {


        if (profileRepository.existsByUserId(user.getId())) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        CompatibilityDto compatibilityDto = requestDto.getCompatibility();
        CompatibilityProfile compatibilityProfile = CompatibilityProfile.builder()
                .lifePriority(compatibilityDto.getLifePriority())
                .weekendStyle(compatibilityDto.getWeekendStyle())
                .conflictStyle(compatibilityDto.getConflictStyle())
                .loveLanguage(compatibilityDto.getLoveLanguage())
                .build();

        IdealTypeFilter idealTypeFilter = null;
        IdealTypeDto idealTypeDto = requestDto.getIdealType();
        if (idealTypeDto != null) {
             idealTypeFilter = IdealTypeFilter.builder()
                    .idealAnimalFace(idealTypeDto.getIdealAnimalFace())
                    .minIncome(idealTypeDto.getMinIncome())
                    .minHeight(idealTypeDto.getMinHeight())
                    .maxHeight(idealTypeDto.getMaxHeight())
                    .build();
        }


        Profile newProfile = Profile.builder()
                .nickname(requestDto.getNickname())
                .gender(requestDto.getGender())
                .birthday(requestDto.getBirthday())
                .place(requestDto.getPlace())
                .selfIntroduction(requestDto.getSelfIntroduction())
                .job(requestDto.getJob())
                .education(requestDto.getEducation())
                .mbti(requestDto.getMbti())
                .height(requestDto.getHeight())
                .income(requestDto.getIncome())
                .user(user)
                .build();

//        log.info(requestDto.getImageUrls().toString());
//        log.info(requestDto.getProfileFavorites().toString());


        newProfile.setCompatibilityProfile(compatibilityProfile);
        if (idealTypeFilter != null) {
            newProfile.setIdealTypeFilter(idealTypeFilter);
        }


        if (requestDto.getImageUrls() != null) {
            for(int i = 0; i < requestDto.getImageUrls().size(); i++) {
                String currentImage =   requestDto.getImageUrls().get(i);

                ProfileImage image = ProfileImage.builder()
                        .imageUrl(currentImage)
                        .displayOrder(i+1)
                        .build();
                newProfile.addImage(image);
            }
        }

        if (requestDto.getProfileFavorites() != null) {
            List<String> favoriteNames = requestDto.getProfileFavorites();
            List<Favorite> favorites = favoriteRepository.findAllByNameIn(favoriteNames);

            if (favorites.size() != favoriteNames.size()) {
                throw new IllegalArgumentException("요청에 존재하지 않는 관심사가 포함되어 있습니다.");
            }
            favorites.stream()
                    .map(ProfileFavorite::new)
                    .forEach(newProfile::addFavorite);
        }
        profileRepository.save(newProfile);
        log.info("새로운 프로필이 생성되었습니다. User Email: {}", user.getEmail());


    }

    @Transactional
    public MyProfileResponse getMyProfile(User user) {
        Profile profile = profileRepository.findProfileWithImagesByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저의 프로필을 찾을 수 없습니다."));


        int age = calculateAge(profile.getBirthday());

        String mainImageUrl = profile.getImages().isEmpty()
                ? "" // 이미지가 없을 경우 빈 문자열 반환
                : profile.getImages().get(0).getImageUrl();

        log.info("프로필 이미지 Url : {}", mainImageUrl);

        return MyProfileResponse.builder()
                .nickname(profile.getNickname())
                .mainImageUrl(mainImageUrl)
                .isOptIn(profile.isOpt())
                .age(age)
                .location(profile.getPlace())
                .build();
    }
    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}
