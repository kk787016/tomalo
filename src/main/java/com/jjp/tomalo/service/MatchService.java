package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.match.DailyMatch;
import com.jjp.tomalo.domain.profiles.Gender;
import com.jjp.tomalo.domain.profiles.Profile;
import com.jjp.tomalo.repository.DailyMatchRepository;
import com.jjp.tomalo.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final ProfileRepository profileRepository;
    private final MatchingScoreService scoreService;
    private final DailyMatchRepository dailyMatchRepository;

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

        List<Profile> men = new ArrayList<>();
        List<Profile> women = new ArrayList<>();

        //젠더로 나누고.
        for (Profile candidate : candidates) {
            if (candidate.getGender().equals(Gender.MALE)){
                men.add(candidate);
            }else {
                women.add(candidate);
            }
        }

        // 최대 커플 수 맞추고.
        int diff = Math.abs(men.size() - women.size());

        // 더미 데이터 채워서 짝수 맞추고.
        if (diff > 0) {
            if (men.size() > women.size()) {
                for (int i = 0; i < diff; i++) {
                    women.add(null);
                }
            } else {
                for (int i = 0; i < diff; i++) {
                    men.add(null);
                }
            }
        }

        // 양쪽 점수 세팅
        Map<Long, Queue<Long>> menPrefs = new HashMap<>(); // 남자가 점수줄 여자 맵
        Map<Long, Map<Long, Double>> womenScores = new HashMap<>(); // 여자가 갖고 있는 남자 맵

        for (Profile man : men){
            List<Profile> rankedWomen = new ArrayList<>(women);
            rankedWomen.sort((w1, w2) -> Double.compare(
                    scoreService.calculateScore(man, w2), // 내림차순
                    scoreService.calculateScore(man, w1)
            ));
            Queue<Long> prefQueue = new LinkedList<>();
            for (Profile w : rankedWomen) prefQueue.add(w.getId());
            menPrefs.put(man.getId(), prefQueue);
        }

        for (Profile woman : women) {
            Map<Long, Double> scores = new HashMap<>();
            for (Profile man : men) {
                scores.put(man.getId(), scoreService.calculateScore(woman, man));
            }
            womenScores.put(woman.getId(), scores);
        }


        // 게일- 섀플리 알고리즘
        Map<Long, Long> womenEngagements = new HashMap<>();
        Set<Long> freeMen = new HashSet<>();

        for (Profile m : men) freeMen.add(m.getId());


        while(!freeMen.isEmpty()){
            Long manId = freeMen.iterator().next();
            Queue<Long> prefs = menPrefs.get(manId);

            if(prefs == null || prefs.isEmpty()){
                freeMen.remove(manId);
                continue;
            }

            Long womanId = prefs.poll();

            if (!womenEngagements.containsKey(womanId)) {
                womenEngagements.put(womanId, manId);
                freeMen.remove(manId);
            }else{
                Long currentFianceId = womenEngagements.get(womanId);
                Map<Long, Double> herScores = womenScores.get(womanId);

                double scoreCurrent = herScores.getOrDefault(currentFianceId, 0.0);
                double scoreNew = herScores.getOrDefault(manId, 0.0);

                if (scoreNew > scoreCurrent) {
                    womenEngagements.put(womanId, manId);
                    freeMen.remove(manId);
                    freeMen.add(currentFianceId);
                }

            }

        }

        saveMatches(womenEngagements, candidates);

    }

    private void saveMatches(Map<Long, Long> engagements, List<Profile> candidates) {
        Map<Long, Profile> profileMap = candidates.stream()
                .collect(Collectors.toMap(Profile::getId, p -> p));

        List<DailyMatch> matchesToSave = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : engagements.entrySet()) {
            Long womanId = entry.getKey();
            Long manId = entry.getValue();

            Profile woman = profileMap.get(womanId);
            Profile man = profileMap.get(manId);

             DailyMatch match = DailyMatch.builder()
                 .profileA(man)
                 .profileB(woman)
                 .matchDate(LocalDate.now())
                 .compatibilityScore((scoreService.calculateScore(man, woman) + scoreService.calculateScore(woman, man)) / 2)
                 .build();
             matchesToSave.add(match);

            log.info("매칭 성사: {} & {}", man.getNickname(), woman.getNickname());
        }

        dailyMatchRepository.saveAll(matchesToSave);

    }
}
