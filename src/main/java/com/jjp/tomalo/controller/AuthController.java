package com.jjp.tomalo.controller;

import com.jjp.tomalo.dto.auth.LocalSignUpRequestDto;
import com.jjp.tomalo.dto.auth.LoginRequestDto;
import com.jjp.tomalo.dto.auth.TokenDto;
import com.jjp.tomalo.dto.auth.TokenReissueRequestDto;
import com.jjp.tomalo.service.AuthService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/local/signup")
    public ResponseEntity<TokenDto> localSignUp(@Valid @RequestBody LocalSignUpRequestDto requestDto){
        TokenDto tokenDto = authService.localSignUp(requestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        TokenDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenReissueRequestDto requestDto) {
        TokenDto tokenDto = authService.reissueToken(requestDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }



}


