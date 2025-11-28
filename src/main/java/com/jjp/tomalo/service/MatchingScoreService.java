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

        double score = 0;

        // 1) 흡연 (10점)
        // preferSmoker: true(흡연자 선호), false(비흡연자 선호), null(상관없음)
        Boolean preferSmoker = myFilter.getPreferSmoker();
        if (preferSmoker == null) {
            score += 10; // 상관없음
        } else {
            // 내 선호(true/false)와 상대방의 실제 흡연여부(isSmoker)가 같으면 점수
            // 예: 비흡연 선호(false) == 비흡연자(false) -> 일치
            if (preferSmoker.equals(target.isSmoker())) {
                score += 10;
            }
        }

        // 2) 종교 (10점)
        String religionPref = myFilter.getReligionPreference();
        if (religionPref == null || "상관없음".equals(religionPref)) {
            score += 10;
        } else {
            // 내 선호 종교와 상대방의 실제 종교가 일치하면 점수
            if (religionPref.equals(target.getReligion())) {
                score += 10;
            }
        }

        // 3) 음주 (10점)
        String drinkingPref = myFilter.getDrinkingPreference();
        if (drinkingPref == null || "상관없음".equals(drinkingPref)) {
            score += 10;
        } else {
            // 내 선호 음주 스타일과 상대방의 실제 음주 스타일이 일치하면 점수
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
