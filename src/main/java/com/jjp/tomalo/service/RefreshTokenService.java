package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.RefreshToken;
import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.dto.auth.TokenDto;
import com.jjp.tomalo.exception.UserNotFoundException;
import com.jjp.tomalo.jwt.JwtTokenProvider;
import com.jjp.tomalo.repository.RefreshTokenRepository;
import com.jjp.tomalo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void saveOrUpdateRefreshToken(Long userId, String tokenValue) {
        RefreshToken refreshToken = new RefreshToken(userId, tokenValue);

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        existingToken -> {
                            log.info("기존 Refresh Token을 갱신합니다. userId: {}", userId);
                            existingToken.updateToken(tokenValue);
                        },
                        () -> {
                            log.info("새로운 Refresh Token을 저장합니다. userId: {}", userId);
                            refreshTokenRepository.save(refreshToken);
                        }
                );
    }

    @Transactional
    public TokenDto reissueToken(String refreshTokenValue) {
        // 1. Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshTokenValue)) {
            throw new BadCredentialsException("유효하지 않은 Refresh Token 입니다.");
        }

        // 2. DB에서 Refresh Token 조회
        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(refreshTokenValue)
                .orElseThrow(() -> new BadCredentialsException("DB에 존재하지 않거나 만료된 Refresh Token 입니다."));

        // 3. 사용자 정보 조회
        Long userId = refreshToken.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다: " + userId));

        // 4. 새로운 토큰 생성
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        TokenDto newTokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 5. DB의 Refresh Token 갱신 (Refresh Token Rotation)
        refreshToken.updateToken(newTokenDto.getRefreshToken());

        log.info("토큰 재발급 성공: userId={}", userId);
        return newTokenDto;
    }
}
