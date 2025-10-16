package com.jjp.tomalo.service;

import com.jjp.tomalo.dto.auth.LocalSignUpRequestDto;
import com.jjp.tomalo.dto.auth.LoginRequestDto;
import com.jjp.tomalo.dto.auth.TokenDto;

public interface AuthService {
    TokenDto localSignUp(LocalSignUpRequestDto requestDto);

    TokenDto login(LoginRequestDto requestDto);

    TokenDto reissueToken(String refreshToken);
}
