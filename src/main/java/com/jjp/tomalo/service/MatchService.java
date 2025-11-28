package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.profiles.Gender;
import com.jjp.tomalo.domain.profiles.Profile;
import com.jjp.tomalo.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<Profile> candidates = profileRepository.findAllByOptStatus(true);

        log.info(String.valueOf(candidates.size()));

        if (candidates.size() < 2) {
            log.info("매칭 후보자가 2명 미만이므로 매칭을 종료합니다.");
            return;
        }
        //여기서 남자 여자 나누고.

        List<Profile> male = new ArrayList<>();
        List<Profile> female = new ArrayList<>();

        //젠더로 나누고.
        for (Profile candidate : candidates) {
            if (candidate.getGender().equals(Gender.MALE)){
                male.add(candidate);
            }else {
                female.add(candidate);
            }
        }

        // 최대 커플 수 맞추고.
        int diff = Math.abs(male.size() - female.size());

        // 더미 데이터 채워서 짝수 맞추고.
        if (diff > 0) {
            if (male.size() > female.size()) {
                for (int i = 0; i < diff; i++) {
                    female.add(null);
                }
            } else {
                for (int i = 0; i < diff; i++) {
                    male.add(null);
                }
            }
        }

        // 일단 각각 점수 부여 함.
        // 점수 매기는 조건들.


        // 더미랑 만나면 매칭 실패.

        // 이대로 결과도 나오는지 확인하기.


    }
}
