package com.jjp.tomalo.handler;

import com.jjp.tomalo.dto.auth.TokenDto;
import com.jjp.tomalo.jwt.JwtTokenProvider;
import com.jjp.tomalo.repository.RefreshTokenRepository;
import com.jjp.tomalo.service.RefreshTokenService;
import com.jjp.tomalo.service.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        refreshTokenService.saveOrUpdateRefreshToken(userPrincipal.getUser().getId(), tokenDto.getRefreshToken());

        // 3. 프론트엔드로 토큰을 전달하기 위한 리디렉션 URL 생성
        String targetUrl = UriComponentsBuilder.fromUriString("tomalo://auth/callback") // 예시: 프론트엔드의 리디렉션 처리 페이지
                .queryParam("accessToken", tokenDto.getAccessToken())
                .queryParam("refreshToken", tokenDto.getRefreshToken())
                .build().toUriString();

        // 4. 생성된 URL로 리디렉션
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
