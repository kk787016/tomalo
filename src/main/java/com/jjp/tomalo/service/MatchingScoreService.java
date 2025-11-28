package com.jjp.tomalo.service;


import com.jjp.tomalo.domain.match.CompatibilityProfile;
import com.jjp.tomalo.domain.match.IdealTypeFilter;
import com.jjp.tomalo.domain.profiles.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingScoreService {
    private final MbtiService mbtiService;

    private static final String NO_PREFERENCE = "상관없음";

    public double calculateScore(Profile me, Profile target) {
        double totalScore = 0;
        // 1. 핵심 가치관 (40점)
        totalScore += calculateValuesScore(me.getCompatibilityProfile(), target.getCompatibilityProfile());

        // 2. 라이프스타일 & 이상형 (30점)
        totalScore += calculateLifestyleScore(me.getIdealTypeFilter(), target);

        // 3. MBTI (20점)
        totalScore += getMbtiScore(me.getMbti(), target.getMbti());

        // 4. 취미 (10점)
        totalScore += calculateHobbyScore(me, target);

        return totalScore;
    }


    private double calculateValuesScore(CompatibilityProfile myCp, CompatibilityProfile targetCp) {
        if (myCp == null || targetCp == null) return 0;
        double score = 0;
        if (myCp.getLifePriority() == targetCp.getLifePriority()) score += 10;
        if (myCp.getWeekendStyle() == targetCp.getWeekendStyle()) score += 10;
        if (myCp.getConflictStyle() == targetCp.getConflictStyle()) score += 10;
        if (myCp.getLoveLanguage() == targetCp.getLoveLanguage()) score += 10;
        return score;
    }
    private double calculateLifestyleScore(IdealTypeFilter myFilter, Profile target) {
        if (myFilter == null) return 15; // 기본 점수 (Null 처리)

        double score = 0;

        // 1) 흡연 (Boolean 타입이라 null 체크로 충분)
        Boolean preferSmoker = myFilter.getPreferSmoker();
        if (preferSmoker == null) {
            score += 10;
        } else {
            if (preferSmoker.equals(target.isSmoker())) {
                score += 10;
            }
        }

        // 2) 종교 (String)
        String religionPref = myFilter.getReligionPreference();
        // ✨ [수정] 문자열 리터럴 대신 상수를 사용
        if (religionPref == null || NO_PREFERENCE.equals(religionPref)) {
            score += 10;
        } else {
            if (religionPref.equals(target.getReligion())) {
                score += 10;
            }
        }

        // 3) 음주 (String)
        String drinkingPref = myFilter.getDrinkingPreference();
        // ✨ [수정] 여기도 상수를 사용
        if (drinkingPref == null || NO_PREFERENCE.equals(drinkingPref)) {
            score += 10;
        } else {
            if (drinkingPref.equals(target.getDrinking())) {
                score += 10;
            }
        }

        return score;
    }
    

    private double getMbtiScore(String m1, String m2) {
        return mbtiService.getMbtiScore(m1, m2);
    }

    private double calculateHobbyScore(Profile me, Profile target) {
        if (me.getProfileFavorites() == null || target.getProfileFavorites() == null) return 0;

        Set<String> myFavs = me.getProfileFavorites().stream()
                .map(pf -> pf.getFavorite().getName())
                .collect(Collectors.toSet());
        Set<String> targetFavs = target.getProfileFavorites().stream()
                .map(pf -> pf.getFavorite().getName())
                .collect(Collectors.toSet());

        long count = myFavs.stream().filter(targetFavs::contains).count();
        return Math.min(count * 2.0, 10.0);
    }
}
