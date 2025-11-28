package com.jjp.tomalo.controller;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.repository.ProfileRepository;
import com.jjp.tomalo.service.MatchService;
import com.jjp.tomalo.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("opt-in")
    public ResponseEntity<Void> optIn (@AuthenticationPrincipal UserPrincipal userPrincipal){

        User user = userPrincipal.getUser();
        matchService.optInProfile(user);
        return ResponseEntity.ok().build();
    }
    @PostMapping("opt-out")
    public ResponseEntity<Void> optOut (@AuthenticationPrincipal UserPrincipal userPrincipal){

        User user = userPrincipal.getUser();
        matchService.optOutProfile(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> matchHome (){
        return ResponseEntity.ok().build();
    }
    // 내 매치 상태 확인 하는 api.
    // 매치가 여러 사람이랑 가능해도 되니까?

    @PostMapping("test")
    public ResponseEntity<Void> test (){
        matchService.createMatches();
        return ResponseEntity.ok().build();
    }

}
