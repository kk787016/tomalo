package com.jjp.tomalo.controller;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.dto.auth.LocalSignUpRequestDto;
import com.jjp.tomalo.dto.auth.ProfileCreateRequestDto;
import com.jjp.tomalo.dto.auth.TokenDto;
import com.jjp.tomalo.dto.profile.MyProfileResponse;
import com.jjp.tomalo.service.ProfileServiceImpl;
import com.jjp.tomalo.service.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid ProfileCreateRequestDto requestDto,

                                       @AuthenticationPrincipal UserPrincipal userPrincipal){

        User user = userPrincipal.getUser();
        profileService.createProfile(user, requestDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("me")
    public ResponseEntity<MyProfileResponse> getMe(@AuthenticationPrincipal UserPrincipal userPrincipal){
        User user = userPrincipal.getUser();
        MyProfileResponse myProfileResponse = profileService.getMyProfile(user);
        return ResponseEntity.ok(myProfileResponse);
    }


}
