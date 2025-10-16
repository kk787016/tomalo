package com.jjp.tomalo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchingScheduler {

    private final MatchService matchService;

    @Scheduled(cron = "0 0 20 * * *")
    public void runMatching() {
        log.info("매칭 스케줄러를 시작합니다.");
        matchService.createMatches();
        log.info("매칭 스케줄러를 종료합니다.");
    }

}
