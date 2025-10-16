package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.match.DailyMatch;
import com.jjp.tomalo.domain.profiles.Profile;
import com.jjp.tomalo.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final ProfileRepository profileRepository;

    @Transactional
    public void optInProfile(User user){
        profileRepository.findByUserId(user.getId()).ifPresent(Profile::setOptIn);
    }

    @Transactional
    public void optOutProfile(User user){
        profileRepository.findByUserId(user.getId()).ifPresent(Profile::setOptOut);
    }


    @Transactional
    public void createMatches() {

        // 여기 참여하기로 한 사람만 뽑기.
//        List<Profile> candidates= profileRepository.findAllByOptInIsTrue();
//        if (candidates.size() < 2) {
//            log.info("매칭 후보자가 2명 미만이므로 매칭을 종료합니다.");
//            return;
//        }
//
//        Set<Long> matchedUserIds = new HashSet<>();
//        List<DailyMatch> newMatches = new ArrayList<>();

    }
}
